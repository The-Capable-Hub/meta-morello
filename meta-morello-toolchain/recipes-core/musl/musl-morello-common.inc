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

# morello-release-1.9
SRCREV    = "e379080e966573c79096f80d89e5b42f469ac8d1"
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
