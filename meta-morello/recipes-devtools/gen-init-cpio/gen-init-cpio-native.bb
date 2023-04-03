inherit native

SUMMARY      = "Gen init cpio"
DESCRIPTION  = "Gen init cpio"
HOMEPAGE     = "http://llvm.org"
LICENSE      = "GPL-2.0-only"

TOOLCHAIN    = "llvm-morello"


OUTPUTS_NAME = "gen-init-cpio"
PROVIDES     = "${OUTPUTS_NAME}-native"

LIC_FILES_CHKSUM  = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI = " \
    git://git.morello-project.org/morello/kernel/linux;protocol=https;nobranch=1 \
    "

SRCREV = "87d06928f90fe910311210a0149d03f3420f593c"

S             = "${WORKDIR}/git/usr"

FILES:${PN}   = "${bindir}/${OUTPUTS_NAME}"

do_configure[noexec] = "1"

do_compile(){
    mkdir -p ${B}/${OUTPUTS_NAME}
    oe_runmake gen_init_cpio
}

do_install(){
    install -d ${D}${bindir}
    install -m 0744 ${S}/gen_init_cpio ${D}${bindir}/gen_init_cpio
}
