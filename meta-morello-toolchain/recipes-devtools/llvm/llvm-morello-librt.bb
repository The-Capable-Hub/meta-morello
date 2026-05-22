inherit cmake

TOOLCHAIN      = "${MORELLO_TOOLCHAIN}"

DESCRIPTION    = "Runtime libraries for C++. Built AFTER musl \
                    and clang."
LICENSE        = "Apache-2.0-with-LLVM-exception"
LIC_FILES_CHKSUM = "file://${LLVM_SHARED_SOURCE}/libcxx/LICENSE.TXT;md5=55d89dd7eec8d3b4204b680e27da3953"

PROVIDES        += "virtual/llvm-morello-librt"

B_LIBRT         = "${WORKDIR}/build_lib_rt"
S_LIBRT         = "${LLVM_SHARED_SOURCE}/runtimes"

SRC_URI[morello-headers.sha256sum] = "ff907d32401bdd7a4f1a0f19e6e6e7a6f607f36dcb180210f53be931e9466951"
SRC_URI += " \
    https://git.morello-project.org/morello/morello-linux-headers/-/archive/morello/master/morello-linux-headers.tar.gz;striplevel=1;subdir=${B_LIBRT};name=morello-headers;downloadfilename=morello-headers.tgz \
"

do_populate_lic[depends] += "llvm-morello-native:do_symlink"

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

LIBUNWIND_HEADERS = "${LLVM_SHARED_SOURCE}/libunwind/include"
LIBCXX_HEADERS = "${LLVM_SHARED_SOURCE}/libcxx/include"

PURECAP_LIBDIR = "${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}/usr/lib"
PURECAP_INCDIR = "${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}/usr/include"

BUILD_FLAGS = "${CC_PURECAP_FLAGS}"
BUILD_FLAGS:remove ="-Werror=format"

CMAKE_TOOLCHAIN_FILE = "${WORKDIR}/lib_rt.cmake"

LIBRT_CMAKE = " \
    -DLLVM_ENABLE_RUNTIMES='libcxx;libcxxabi;libunwind' \
    -DCMAKE_INSTALL_RPATH='$ORIGIN/../lib:${PURECAP_LIBDIR}' \
    -Wno-dev \
    -DCMAKE_TOOLCHAIN_FILE='${CMAKE_TOOLCHAIN_FILE}' \
    -DCMAKE_BUILD_TYPE=Release \
    -DLIBUNWIND_ENABLE_THREADS=ON \
    -DLIBCXX_ENABLE_STATIC=ON \
    -DLIBCXX_ENABLE_SHARED=ON \
    -DLIBCXX_INCLUDE_TESTS=ON \
    -DLIBCXX_INCLUDE_BENCHMARKS=OFF \
    -DLIBCXX_ENABLE_EXPERIMENTAL_LIBRARY=NO \
    -DLIBCXXABI_USE_LLVM_UNWINDER=ON \
    -DLIBUNWIND_USE_COMPILER_RT=ON \
    -DLIBCXXABI_USE_COMPILER_RT=ON \
    -DLIBCXX_USE_COMPILER_RT=ON \
    -DLIBCXXABI_LIBUNWIND_INCLUDES='${LIBUNWIND_HEADERS}' \
    -DLIBCXXABI_LIBUNWIND_PATH='${PURECAP_LIBDIR}' \
    -DLIBCXXABI_LIBCXX_INCLUDES='${LIBCXX_HEADERS}' \
    -DLIBCXX_HAS_MUSL_LIBC=ON \
    -DLIBCXX_ENABLE_EXCEPTIONS=ON \
    -DCMAKE_SYSROOT='${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}' \
    -DLIBCXX_CXX_ABI='libcxxabi' \
    -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF \
    -DLIBCXX_INSTALL_INCLUDE_TARGET_DIR='${D}${PURECAP_SYSROOT_DIR}/usr/include/c++/v1' \
    -DCMAKE_INSTALL_PREFIX='${D}${PURECAP_SYSROOT_DIR}/usr' \
    -DLIBCXX_TARGET_INFO='libcxx.test.target_info.LinuxLocalTI' \
    -DLIBCXX_CXX_ABI_LIBRARY_PATH='${PURECAP_LIBDIR}' \
    "

INHIBIT_DEFAULT_DEPS        = "1"
# buildpaths are left in the so and a. I can't remove them.
INSANE_SKIP:${PN}-staticdev += "buildpaths"
INSANE_SKIP:${PN}-dev += "buildpaths"

do_configure() {

    local llvmversion=$(${CC} ${ccflags} --version)
    local resourcedir=$(${CC} -print-resource-dir)
    local destdir="${D}${PURECAP_SYSROOT_DIR}/usr/lib"
    local builddir="${B_LIBRT}/"

    install -d ${destdir}
    mkdir -p ${builddir}
    mkdir -p ${D}${PREFIX}

    echo "Version: ${llvmversion}"
    echo "ResourceDir: ${resourcedir}"

    cat << EOF > ${CMAKE_TOOLCHAIN_FILE}
set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SYSTEM_PROCESSOR aarch64)
set(CMAKE_ASM_COMPILER_TARGET "${GLOBAL_LIB_TRIPLE}")
set(CMAKE_C_COMPILER_TARGET "${GLOBAL_LIB_TRIPLE}")
set(CMAKE_CXX_COMPILER_TARGET "${GLOBAL_LIB_TRIPLE}")

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

set(CMAKE_EXE_LINKER_FLAGS      "${LD_PURECAP_FLAGS}" CACHE STRING "" FORCE)
set(CMAKE_SHARED_LINKER_FLAGS   "${LD_PURECAP_FLAGS} -nostdlib -nostdlib++  -fdebug-prefix-map=${WORKDIR}=. -fmacro-prefix-map=${WORKDIR}=." CACHE STRING "" FORCE)
include_directories("${B_LIBRT}/usr/include")
EOF

    local config_librt="${LIBRT_CMAKE}"
    cmake -S ${S_LIBRT} -B ${builddir} ${config_librt}
}

do_compile(){
    local builddir="${B_LIBRT}/"
    cd ${builddir}
    make ${PARALLEL_MAKE}
}

do_install() {
    local builddir="${B_LIBRT}/"
    cd ${builddir}
    make install
}

FILES:${PN} = "${PURECAP_SYSROOT_DIR}/usr/lib/*.so ${PURECAP_SYSROOT_DIR}/usr/lib/*.so.* "
FILES:${PN}-staticdev = "${PURECAP_SYSROOT_DIR}/usr/lib/*.a"
FILES:${PN}-dev = "${PURECAP_SYSROOT_DIR}/usr/include/* ${PURECAP_SYSROOT_DIR}/usr/lib/*.so*"

INSANE_SKIP:${PN}    += "file-rdeps"
do_package_qa[noexec] = "1"
