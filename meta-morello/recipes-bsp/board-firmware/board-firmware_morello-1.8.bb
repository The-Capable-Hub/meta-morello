COMPATIBLE_MACHINE = "morello"
SUMMARY            = "Board firmware for Morello"
DESCRIPTION        = "The SD card image from ARM that contains non compile-able binaries."
HOMEPAGE           = "https://git.morello-project.org/morello/board-firmware"
LICENSE            = "STM-SLA0044-Rev5 & BSD-3-Clause & BSD-2-Clause"
OUTPUTS_NAME       = "board-firmware"
SECTION            = "firmware"

PROVIDES          += "virtual/${OUTPUTS_NAME}"

PACKAGE_ARCH       = "${MACHINE_ARCH}"

SRC_URI = "git://git.morello-project.org/morello/board-firmware;protocol=https;branch=${SRCBRANCH}"
SRCREV  = "3f0701022efbbd5d245b439688e5d8408fd9dc5a"

SRCBRANCH        = "morello/release-1.8"
LIC_FILES_CHKSUM = "file://LICENSES/MB/STM.TXT;md5=47185091e44d729bf62ed5c99d7eb9d9 \
                    file://LICENSES/LIB/sensor.txt;md5=9326703f1725982ef3a268f53ccd1883 \
                    "

S                      = "${WORKDIR}/git"
FILES:${PN}           += "/${OUTPUTS_NAME}"
FILES:${PN}-staticdev += "/${OUTPUTS_NAME}/LIB/sensor.a"
SYSROOT_DIRS          += "/${OUTPUTS_NAME}"

do_install() {
    install -d "${D}/${OUTPUTS_NAME}"
    cp -rf ${S}/* "${D}/${OUTPUTS_NAME}/"
}
