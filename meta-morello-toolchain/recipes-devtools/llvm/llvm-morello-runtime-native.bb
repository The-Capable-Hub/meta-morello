inherit cmake native
require recipes-core/musl/musl-morello-${MORELLO_ARCH}.inc

TOOLCHAIN      = "${MORELLO_TOOLCHAIN}"

DESCRIPTION    = "Libraries that go into the clang resource folder, \
                  search path for that folder is relative to clang directory itself."

DEPENDS       += "musl-morello-native"
PROVIDES       = "virtual/llvm-morello-runtime-native"

B_COMPILERRT   = "${WORKDIR}/build_compiler_rt"
S_CRT          = "${LLVM_SHARED_SOURCE}/compiler-rt/lib/crt"
S_COMPILER_RT  = "${LLVM_SHARED_SOURCE}/compiler-rt"

RDEPENDS:${PN}:remove:toolchain-llvm-morello = " musl"

do_install[depends] += "llvm-morello-native:do_symlink"

FILES:${PN}       += "$(${CC} -print-resource-dir)/lib/${LIB_TRIPLE}"
INSANE_SKIP:${PN} += "$(${CC} -print-resource-dir)/lib/${LIB_TRIPLE}"

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

CC:remove      = "${CC_PURECAP_FLAGS}"
CXX:remove     = "${CC_PURECAP_FLAGS}"
LDFLAGS:remove = "${LD_PURECAP_FLAGS}"

COMPILERRT_CMAKE = "-Wno-dev \
    -DCMAKE_SYSTEM_NAME=Linux \
    -DCMAKE_TOOLCHAIN_FILE='${WORKDIR}/compiler_rt.cmake' \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_EXPORT_COMPILE_COMMANDS=ON \
    -DCMAKE_SKIP_BUILD_RPATH=OFF \
    -DCMAKE_BUILD_WITH_INSTALL_RPATH=ON \
    -DLLVM_TARGETS_TO_BUILD='AArch64' \
    -DLLVM_ENABLE_ASSERTIONS=OFF \
    -DBUILD_SHARED_LIBS=ON \
    -DCOMPILER_RT_BUILD_BUILTINS=ON \
    -DCOMPILER_RT_BUILD_SANITIZERS=OFF \
    -DCOMPILER_RT_BUILD_XRAY=OFF \
    -DCOMPILER_RT_BUILD_LIBFUZZER=OFF \
    -DCOMPILER_RT_BUILD_PROFILE=OFF \
    -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${LIB_TRIPLE} \
    -DCMAKE_INSTALL_RPATH=\$ORIGIN/../lib \
    -DTARGET_TRIPLE=${LIB_TRIPLE} \
"

do_install() {

    export CFLAGS=""

    local target="${LIB_TRIPLE}"
    local sysroot="${STAGING_LIBDIR_NATIVE}/musl-morello-native/${ARCH_TRIPLE}"

    local ccflags="--target=${target} ${ARCH_FLAGS} -nostdinc -isystem ${sysroot}/include"

    local llvmversion=$(${CC} ${ccflags} --version)
    local resourcedir=$(${CC} -print-resource-dir)

    local destdir="${resourcedir}/lib/${LIB_TRIPLE}"
    local builddir="${B_COMPILERRT}/${ARCH_TRIPLE}"

    install -d ${destdir}
    mkdir -p ${builddir}

    echo "Version: ${llvmversion}"
    echo "ResourceDir: ${resourcedir}"

    ${CC} ${ccflags} -c ${S_CRT}/crtbegin.c -o ${destdir}/clang_rt.crtbegin.o
    ${CC} ${ccflags} -c ${S_CRT}/crtend.c -o ${destdir}/clang_rt.crtend.o


    echo "Cmake config of RT " 

  cat << EOF > ${WORKDIR}/compiler_rt.cmake
set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SYSTEM_PROCESSOR aarch64)
set(CMAKE_ASM_COMPILER_TARGET "${LIB_TRIPLE} ${ARCH_FLAGS}")
set(CMAKE_C_COMPILER_TARGET "${LIB_TRIPLE} ${ARCH_FLAGS}")

set(CMAKE_ASM_COMPILER_WORKS 1 CACHE INTERNAL "")
set(CMAKE_C_COMPILER_WORKS 1 CACHE INTERNAL "")
set(CMAKE_CXX_COMPILER_WORKS 1 CACHE INTERNAL "")

set(CMAKE_ASM_COMPILER "${BUILD_CC}" CACHE FILEPATH "" FORCE)
set(CMAKE_C_COMPILER "${BUILD_CC}" CACHE FILEPATH "" FORCE)
set(CMAKE_CXX_COMPILER "${BUILD_CXX}" CACHE FILEPATH "" FORCE)
set(CMAKE_AR "${BUILD_AR}" CACHE FILEPATH "" FORCE)
set(CMAKE_RANLIB "${BUILD_RANLIB}" CACHE FILEPATH "" FORCE)
set(CMAKE_NM "${BUILD_NM}" CACHE FILEPATH "" FORCE)
set(CMAKE_LINKER "${BUILD_LD}" CACHE FILEPATH "" FORCE)
set(CMAKE_OBJDUMP "${BUILD_OBJDUMP}" CACHE FILEPATH "" FORCE)
set(CMAKE_OBJCOPY "${BUILD_OBJCOPY}" CACHE FILEPATH "" FORCE)

set(LLVM_CONFIG_PATH "${LLVM_CONFIG}" CACHE FILEPATH "" FORCE)
set(CMAKE_ASM_FLAGS "-nostdinc -isystem ${sysroot}/include" CACHE STRING "" FORCE)
set(CMAKE_C_FLAGS "-nostdinc" CACHE STRING "" FORCE)
include_directories("${sysroot}/include")
EOF

    local config_rt="${COMPILERRT_CMAKE}"
    cmake -S ${S_COMPILER_RT} -B ${builddir} ${config_rt}  
    
    echo "Cmake build of RT " 

    cd ${builddir}
    make ${PARALLEL_MAKE} clang_rt.builtins-aarch64

    install ${builddir}/lib/linux/libclang_rt.builtins-aarch64.a ${destdir}/libclang_rt.builtins.a

    install -d ${D}${libdir}/clang/${LLVM_VERSION}/lib/${LIB_TRIPLE}
    local install_dir=${D}${libdir}/clang/${LLVM_VERSION}/lib/${LIB_TRIPLE}

    install ${destdir}/clang_rt.crtbegin.o ${install_dir}/clang_rt.crtbegin.o
    install ${destdir}/clang_rt.crtend.o ${install_dir}/clang_rt.crtend.o
    install ${destdir}/libclang_rt.builtins.a ${install_dir}/libclang_rt.builtins.a
}