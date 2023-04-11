COMPATIBLE_MACHINE = "morello"
SUMMARY            = "Morello initramfs"
LICENSE            = "MIT"
LIC_FILES_CHKSUM   = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

RDEPENDS:${PN} += "busybox-morello"

SRC_URI = "file://files/init.sh \
          "

S = "${WORKDIR}"

do_install() {
    install -m 0755 ${WORKDIR}/files/init.sh ${D}/init
}

FILES:${PN} = "/init"