DESCRIPTION        = "CHERI enabled musl libc"
OUTPUTS_NAME       = "libc"
LICENSE            = "MIT"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

DEPENDS:remove:toolchain-llvm-morello        = "virtual/musl-morello"
RDEPENDS:${PN}:remove:toolchain-llvm-morello = "musl"

SUMMARY = "CHERI enabled musl libc"
SRC_URI = " \
    git://git.morello-project.org/morello/musl-libc;protocol=https;branch=${SRCBRANCH} \
    file://files/compiler_rt.cmake \
	"

# morello-release-1.7
SRCREV    = "acfb827ad04d98ec5c0f558932e29b3c76de3093"
SRCBRANCH = "morello/master"

LIC_FILES_CHKSUM  = "file://COPYRIGHT;md5=b03f1cc25363d094011f8f4fd8bcfb68"

S            = "${WORKDIR}/git"
B            = "${WORKDIR}/build"

TARGET_INSTALL_DIR = "${D}${PURECAP_SYSROOT_DIR}"

INHIBIT_SYSROOT_STRIP       = "1"
INHIBIT_PACKAGE_STRIP       = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

CC:remove      = "${CC_PURECAP_FLAGS}"
CXX:remove     = "${CC_PURECAP_FLAGS}"
LDFLAGS:remove = "${LD_PURECAP_FLAGS}"

CONFIGUREOPTS = " \
    --prefix=${prefix} \
    --exec-prefix=${exec_prefix} \
    --bindir=${bindir} \
    --libdir=${libdir} \
    --includedir=${includedir} \
    --syslibdir=${nonarch_base_libdir} \
    --target=${ARCH_TRIPLE} \
    --disable-libshim \
"

COMPILERRT_CMAKE = "-Wno-dev \
    -DCMAKE_TOOLCHAIN_FILE=compiler_rt.cmake \
    -DCMAKE_BUILD_TYPE=Release \
    -DLLVM_CONFIG_PATH='${LLVM_CONFIG}' \
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
"
do_configure() {
    export CFLAGS=""
    local config="${CONFIGUREOPTS}"
    echo "Install dir ${TARGET_INSTALL_DIR} with ${TCLIBC} MACHINE_INC ${MACHINE_INC}"
    config="${config} --target=${ARCH_TRIPLE} ${EXTRA_CONFIGUREOPTS} --disable-shared"
    ${S}/configure ${config}
}

do_compile() {
    export CFLAGS=""
    oe_runmake
}
