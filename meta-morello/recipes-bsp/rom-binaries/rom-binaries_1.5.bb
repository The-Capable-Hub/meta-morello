inherit deploy

COMPATIBLE_MACHINE = "morello-fvp"
SUMMARY            = "ROM binaries for Morello"
DESCRIPTION        = "These are the ROM binaries that went into Morello silicon and we need to use the same on FVP to avoid deviations in the software stack."
HOMEPAGE           = "https://git.morello-project.org/morello/rom-binaries"
LICENSE            = "BSD-3-Clause"

PROVIDES           = "virtual/${BPN}"

PACKAGE_ARCH       = "${MACHINE_ARCH}"

SRC_URI = "git://git.morello-project.org/morello/rom-binaries;protocol=https;branch=${SRCBRANCH}"
SRCREV  = "c86e6a17563423d3938240799196ec68784f9e04"

SRCBRANCH        = "morello/release-1.5"

LIC_FILES_CHKSUM = "file://LICENSES/bl1.txt;md5=8737ebfecb4a75675c676c10f7d07c70 \
                    file://LICENSES/mcp_rom.txt;md5=ad686ac8d50ed47c030a1094ffdead49 \
                    file://LICENSES/scp_rom.txt;md5=f4d01c71878528d6bb100cb606db590c \
                    "

S = "${WORKDIR}/git"

do_install () {
    install ${S}/bl1.bin ${D}/bl1.bin
    install ${S}/scp_romfw.bin ${D}/scp_romfw.bin
    install ${S}/mcp_romfw.bin ${D}/mcp_romfw.bin
}

do_deploy() {
    install -d ${DEPLOYDIR}/rom-binaries
    install ${S}/bl1.bin ${DEPLOYDIR}/rom-binaries/bl1.bin
    install ${S}/scp_romfw.bin ${DEPLOYDIR}/rom-binaries/scp_romfw.bin
    install ${S}/mcp_romfw.bin ${DEPLOYDIR}/rom-binaries/mcp_romfw.bin
}
addtask deploy after do_install