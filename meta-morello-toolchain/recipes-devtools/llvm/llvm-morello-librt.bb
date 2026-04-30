inherit cmake
require recipes-core/musl/musl-morello-${MORELLO_ARCH}.inc

TOOLCHAIN      = "${MORELLO_TOOLCHAIN}"

DESCRIPTION    = "Runtime libraries for C++. Built AFTER musl \
                    and clang."

PROVIDES        += "virtual/llvm-morello-librt"
DEPENDS         += " llvm-morello "
RDEPENDS:${PN}  += " llvm-morello "
RDEPENDS:${PN}:remove:toolchain-llvm-morello = " musl "

do_install[depends] += "llvm-morello-native:do_symlink"

B_LIBRT         = "${WORKDIR}/build_lib_rt"
S_LIBRT         = "${LLVM_SHARED_SOURCE}/runtimes"

SRC_URI[morello-headers.sha256sum] = "ff907d32401bdd7a4f1a0f19e6e6e7a6f607f36dcb180210f53be931e9466951"
SRC_URI += " \
    https://git.morello-project.org/morello/morello-linux-headers/-/archive/morello/master/morello-linux-headers.tar.gz;striplevel=1;subdir=${B_LIBRT};name=morello-headers;downloadfilename=morello-headers.tgz \
"

PREFIX = "${libdir}/${LIB_TRIPLE}"
COMPILER_RT_PATH = "${STAGING_LIBDIR}/clang/${LLVM_VERSION}/lib/${LIB_TRIPLE}"

SYSROOT = "${STAGING_LIBDIR_NATIVE}/musl-morello-native/${ARCH_TRIPLE}" 

TFLAGS="-march=morello -mabi=purecap"

BUILD_CC          = "${LLVM_PATH}/clang"
BUILD_CXX         = "${LLVM_PATH}/clang++"
BUILD_CPP         = "${LLVM_PATH}/clang"
BUILD_CCLD        = "${LLVM_PATH}/clang"
BUILD_RANLIB      = "${LLVM_PATH}/llvm-ranlib"
BUILD_AR          = "${LLVM_PATH}/llvm-ar"
BUILD_AS          = "${LLVM_PATH}/llvm-as"
BUILD_NM          = "${LLVM_PATH}/llvm-nm"
BUILD_OBJDUMP     = "${LLVM_PATH}/llvm-objdump"
BUILD_OBJCOPY     = "${LLVM_PATH}/llvm-objcopy"
BUILD_STRIP       = "${LLVM_PATH}/llvm-strip"
BUILD_STRINGS     = "${LLVM_PATH}/llvm-strings"
BUILD_READELF     = "${LLVM_PATH}/llvm-readelf"
BUILD_LD          = "${LLVM_PATH}/ld.lld"
BUILD_LTO         = "-fuse-ld=lld"
BUILD_HOSTCC      = "${LLVM_PATH}/clang"

BUILD_FLAGS = "--sysroot=${SYSROOT} ${TFLAGS} -isystem ${SYSROOT}/include -fdebug-prefix-map=${WORKDIR}= -fmacro-prefix-map=${WORKDIR}= "

LIBUNWIND_HEADERS = "${LLVM_SHARED_SOURCE}/libunwind/include"
LIBCXX_HEADERS = "${STAGING_INCIDR}/c++/v1"

CMAKE_TOOLCHAIN_FILE = "${WORKDIR}/lib_rt.cmake"

LIBRT_CMAKE = "-Wno-dev \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_SYSROOT='${SYSROOT}' \
    -DCMAKE_BUILD_WITH_INSTALL_RPATH=ON \
    -DCMAKE_SKIP_BUILD_RPATH=OFF \
    -DBUILD_SHARED_LIBS=ON \
    -DCMAKE_INSTALL_DO_STRIP=1 \
    -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
    -DCMAKE_INSTALL_RPATH='$ORIGIN/../lib:${LIB_PATH}' \
    -DCMAKE_EXPORT_COMPILE_COMMANDS=ON \
    -DCMAKE_VERBOSE_MAKEFILE=ON \
    -DLLVM_ENABLE_RUNTIMES='libcxx;libcxxabi;libunwind' \
    -DCMAKE_TOOLCHAIN_FILE='${CMAKE_TOOLCHAIN_FILE}' \
    -DCMAKE_INSTALL_PREFIX=${D}${PREFIX} \
    -DLIBCXX_ENABLE_STATIC=ON \
    -DLIBCXX_ENABLE_SHARED=ON \
    -DLIBCXX_INCLUDE_BENCHMARKS=OFF \
    -DLIBCXX_USE_COMPILER_RT=ON \
    -DLIBCXX_ENABLE_EXCEPTIONS=ON \
    -DLIBCXX_CXX_ABI='libcxxabi' \
    -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF \
    -DLIBCXX_INSTALL_INCLUDE_TARGET_DIR='${B_LIBRT}/include/${LIB_TRIPLE}/c++/v1' \
    -DLIBCXX_TARGET_INFO='libcxx.test.target_info.LinuxLocalTI' \
    -DLIBCXXABI_USE_LLVM_UNWINDER=ON \
    -DLIBCXXABI_USE_COMPILER_RT=ON \
    -DLIBCXXABI_LIBUNWIND_PATH='${SYSROOT}/lib' \
    -DLIBCXXABI_LIBUNWIND_INCLUDES='${LIBUNWIND_HEADERS}' \
    -DLIBCXXABI_LIBCXX_INCLUDES='${LIBCXX_HEADERS}' \
    -DLIBCXX_CXX_ABI_LIBRARY_PATH='${SYSROOT}/lib' \
    -DLIBUNWIND_ENABLE_THREADS=ON \
    -DLIBUNWIND_USE_COMPILER_RT=ON \
    -DCXX_SUPPORTS_FNO_EXCEPTIONS_FLAG=ON \
    -DCXX_SUPPORTS_FUNWIND_TABLES_FLAG=ON \
    -DLIBCXX_HAS_MUSL_LIBC=ON \
    "

INHIBIT_DEFAULT_DEPS        = "1"
# buildpaths are left in the so and a. I can't remove them.
INSANE_SKIP:${PN}-staticdev += "buildpaths"
INSANE_SKIP:${PN}-dev += "buildpaths"

do_install() {

    export CFLAGS=""

    local target="${LIB_TRIPLE}"
    local ccflags="--target=${target} ${ARCH_FLAGS} -nostdinc -isystem ${SYSROOT}/include"
    local llvmversion=$(${CC} ${ccflags} --version)
    local resourcedir=$(${CC} -print-resource-dir)
    local destdir="${resourcedir}/usr/lib/${LIB_TRIPLE}"
    local builddir="${B_LIBRT}/"

    install -d ${destdir}
    mkdir -p ${builddir}
    mkdir -p ${D}${PREFIX}

    echo "Version: ${llvmversion}"
    echo "ResourceDir: ${resourcedir}"

    cat << EOF > ${CMAKE_TOOLCHAIN_FILE}
set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SYSTEM_PROCESSOR aarch64)
set(CMAKE_ASM_COMPILER_TARGET "${LIB_TRIPLE}")
set(CMAKE_C_COMPILER_TARGET "${LIB_TRIPLE}")
set(CMAKE_CXX_COMPILER_TARGET "${LIB_TRIPLE}")

set(CMAKE_ASM_COMPILER_WORKS 1 CACHE INTERNAL "")
set(CMAKE_C_COMPILER_WORKS 1 CACHE INTERNAL "")
set(CMAKE_CXX_COMPILER_WORKS 1 CACHE INTERNAL "")

set(CMAKE_ASM_COMPILER  "${BUILD_CC}" CACHE FILEPATH "" FORCE)
set(CMAKE_C_COMPILER    "${BUILD_CC}" CACHE FILEPATH "" FORCE)
set(CMAKE_CXX_COMPILER  "${BUILD_CXX}" CACHE FILEPATH "" FORCE)
set(CMAKE_AR            "${BUILD_AR}" CACHE FILEPATH "" FORCE)
set(CMAKE_RANLIB        "${BUILD_RANLIB}" CACHE FILEPATH "" FORCE)
set(CMAKE_NM            "${BUILD_NM}" CACHE FILEPATH "" FORCE)
set(CMAKE_LINKER        "${BUILD_LD}" CACHE FILEPATH "" FORCE)
set(CMAKE_OBJDUMP       "${BUILD_OBJDUMP}" CACHE FILEPATH "" FORCE)
set(CMAKE_OBJCOPY       "${BUILD_OBJCOPY}" CACHE FILEPATH "" FORCE)

set(LLVM_CONFIG_PATH "${LLVM_CONFIG}" CACHE FILEPATH "" FORCE) # verified
set(CMAKE_ASM_FLAGS "${BUILD_FLAGS}" CACHE STRING "" FORCE)
set(CMAKE_C_FLAGS   "${BUILD_FLAGS}" CACHE STRING "" FORCE)
set(CMAKE_CXX_FLAGS "${BUILD_FLAGS}" CACHE STRING "" FORCE)

set(CMAKE_EXE_LINKER_FLAGS      "${BUILD_LTO} --rtlib=compiler-rt" CACHE STRING "" FORCE)
set(CMAKE_SHARED_LINKER_FLAGS   "${BUILD_LTO} -nostdlib -nostdlib++ --rtlib=compiler-rt -fdebug-prefix-map=${WORKDIR}=. -fmacro-prefix-map=${WORKDIR}=." CACHE STRING "" FORCE)
include_directories("${B_LIBRT}/usr/include")
EOF

    local config_librt="${LIBRT_CMAKE}"
    cmake -S ${S_LIBRT} -B ${builddir} ${config_librt}
    cd ${builddir}
    make ${PARALLEL_MAKE} cxx cxxabi
    make install
}

FILES:${PN} = "${libdir}/${LIB_TRIPLE}/lib/*.so"
FILES:${PN} = "${libdir}/${LIB_TRIPLE}"
FILES:${PN} += "${libdir}/${LIB_TRIPLE}/include/"
FILES:${PN}-staticdev = "${libdir}/${LIB_TRIPLE}/lib/*.a"
FILES:${PN}-dev = "${libdir}/${LIB_TRIPLE}/lib/*.so*"

FILES:${PN}       += "$(${CC} -print-resource-dir)/lib/${LIB_TRIPLE}"
INSANE_SKIP:${PN} += "$(${CC} -print-resource-dir)/lib/${LIB_TRIPLE}"
