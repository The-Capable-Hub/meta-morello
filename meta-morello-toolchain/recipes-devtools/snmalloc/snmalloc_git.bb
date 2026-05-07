inherit cmake

SUMMARY = "A message passing based allocator"
DESCRIPTION = "snmalloc is a research allocator, which implements a message passing based design."
HOMEPAGE = "https://github.com/microsoft/snmalloc"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b98fddd052bb2f5ddbcdbd417ffb26a8"

TOOLCHAIN      = "${MORELLO_TOOLCHAIN}"
COMPATIBLE_MACHINE = "morello"

SRC_URI = "git://github.com/microsoft/snmalloc.git;protocol=https;branch=main"
SRCREV = "cd2b19b9f1c8db1df7d97a97ed6660cc16abd6de"
SRC_URI[sha256sum] = "5e9db0d6a250c4c3e7a5b1d61bf6966fa67a1ad5d4aa050e779d0aa594055116"

S = "${WORKDIR}/git"

DEPENDS = " llvm-morello llvm-morello-librt "
RDEPENDS:${PN} = " llvm-morello llvm-morello-librt "

SYSROOT = "${STAGING_LIBDIR}/musl-morello-native/${ARCH_TRIPLE}"

# Often used to override malloc
# PACKAGECONFIG ??= "enable-alloc-override"
# PACKAGECONFIG[enable-alloc-override] = "-DSNMALLOC_OVERRIDE_MALLOC=ON,-DSNMALLOC_OVERRIDE_MALLOC=OFF"

BUILD_FLAGS = "\
    --sysroot=${STAGING_DIR} \
    -nostdlib -nostdlib++ \
    -march=morello -mabi=purecap -Xclang \
    -morello-vararg=new -std=c++17 \
    "

# -isystem ${SYSROOT}/include \
# -isystem ${STAGING_LIBDIR}/${LIB_TRIPLE}/include 
# -isystem ${STAGING_LIBDIR}/${ARCH_TRIPLE}/include 
# -L${STAGING_LIBDIR}/${LIB_TRIPLE}/lib 
# -L${STAGING_LIBDIR}/${ARCH_TRIPLE}/lib 
# -I${STAGING_INCDIR} 
# -I${STAGING_INCDIR_NATIVE} 

EXTRA_OECMAKE += " \
    -DCMAKE_SYSTEM_NAME='Linux' \
    -DCMAKE_SYSTEM_PROCESSOR='arch64' \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_EXPORT_COMPILE_COMMANDS=ON \
    -DCMAKE_VERBOSE_MAKEFILE=ON \
    -DLLVM_CONFIG_PATH='${LLVM_CONFIG}' \
    -DCMAKE_ASM_COMPILER_TARGET='${LIB_TRIPLE}' \
    -DCMAKE_C_COMPILER_TARGET='${LIB_TRIPLE}' \
    -DCMAKE_CXX_COMPILER_TARGET='${LIB_TRIPLE}' \
    -DCMAKE_ASM_COMPILER_WORKS='1' \
    -DCMAKE_C_COMPILER_WORKS='1' \
    -DCMAKE_CXX_COMPILER_WORKS='1' \
    -DCMAKE_SYSROOT='${STAGING_DIR}' \
    -DCMAKE_CXX_FLAGS='${BUILD_FLAGS}' \
    -DCMAKE_C_FLAGS='${BUILD_FLAGS}' \
    -DCMAKE_SYSROOT='${SYSROOT}' \
    -DCMAKE_INCLUDE_PATH='${STAGING_INCDIR}' \
    "

# -DCMAKE_CXX_COMPILER=${LLVM_PATH}/clang++ 
# -DCMAKE_C_COMPILER=${LLVM_PATH}/clang 
# -DCMAKE_LINKER='${LLVM_PATH}/ld.lld' 

# snmalloc is a header-only library + a small static library in some configurations
# It does not create a .so file by default, it is usually linked statically.
# We ensure headers and static libs are installed.

do_install() {
    install -d ${D}${includedir}/snmalloc
    cp -r ${S}/src/* ${D}${includedir}/snmalloc/

    # If a static library is produced, install it:
    # cmake_do_install automatically handles some of this,
    # but snmalloc is often used via inclusion.
}
