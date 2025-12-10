inherit pure-cap-kheaders

COMPATIBLE_MACHINE = "morello"
SUMMARY            = "CHERI enabled busybox"
LICENSE            = "GPL-2.0-only"
SECTION            = "base"

TOOLCHAIN          = "${MORELLO_TOOLCHAIN}"

FILESEXTRAPATHS:prepend := "${THISDIR}:${THISDIR}/${PN}:"

PROVIDES += "virtual/morello-busybox"

RPROVIDES:${PN} += "busybox-morello"

SRC_URI = " \
    git://git.morello-project.org/morello/morello-busybox;protocol=https;branch=${SRCBRANCH} \
    file://files/0001-makefile-add-LLVM_PATH.patch \
    "

PV  = "git${SRCPV}"
S   = "${WORKDIR}/git"

EXTRA_OEMAKE = "\
                MUSL_HOME='${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}${prefix}' \
                KHEADERS='${STAGING_DIR_TARGET}${PURECAP_SYSROOT_DIR}${prefix}/src/linux-headers-morello/include' \
            "

FILES:${PN}  += "/busybox-morello/busybox ${PURECAP_SYSROOT_DIR}/bin"
SYSROOT_DIRS += "/busybox-morello"

INHIBIT_SYSROOT_STRIP       = "1"
INHIBIT_PACKAGE_STRIP       = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

do_configure() {
    export CFLAGS=""
    oe_runmake ${EXTRA_OEMAKE} LLVM_PATH=${LLVM_PATH} morello_busybox_defconfig
}

do_compile() {
    export CFLAGS=""
    local resourcedir=$(${CC} -print-resource-dir)
    EXTRA_OEMAKE="${EXTRA_OEMAKE} CLANG_RESOURCE_DIR='${resourcedir}'"
    oe_runmake ${EXTRA_OEMAKE} LLVM_PATH=${LLVM_PATH}
}

do_install() {
    install -d ${D}/busybox-morello ${D}${PURECAP_SYSROOT_DIR}/bin
    install -m 0755  ${S}/busybox ${D}/busybox-morello/busybox
    install -m 0755  ${S}/busybox ${D}${PURECAP_SYSROOT_DIR}/bin
}
