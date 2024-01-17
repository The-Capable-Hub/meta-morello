inherit python3native
require recipes-bsp/uefi/edk2-firmware.inc
COMPATIBLE_MACHINE = "morello"
SUMMARY            = "EDK2 to be compiled with LLVM Morello"
OUTPUTS_NAME       = "edk2-firmware"
SECTION            = "firmware"

TOOLCHAIN          = "${MORELLO_TOOLCHAIN}"

MACHINE_EDK2_REQUIRE ?= ""
MACHINE_EDK2_REQUIRE:morello-fvp = "edk2-firmware-morello-fvp.inc"
MACHINE_EDK2_REQUIRE:morello-soc = "edk2-firmware-morello-soc.inc"
require ${MACHINE_EDK2_REQUIRE}

PROVIDES          += "virtual/${OUTPUTS_NAME}"
DEPENDS           += "acpica-native python3-native"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

SRC_URI = "\
    ${EDK2_SRC_URI};name=edk2;destsuffix=edk2 \
    ${EDK2_PLATFORMS_SRC_URI};name=edk2-platforms;destsuffix=edk2/edk2-platforms \
    ${EDK2_NON_OSI_SRC_URI};name=edk2-non-osi;destsuffix=edk2-non-osi \
    "

EDK2_SRC_URI           = "gitsm://git.morello-project.org/morello/edk2;branch=morello/release-1.7;protocol=https"
EDK2_PLATFORMS_SRC_URI = "gitsm://git.morello-project.org/morello/edk2-platforms;branch=morello/release-1.7;protocol=https"
EDK2_NON_OSI_SRC_URI   = "git://github.com/tianocore/edk2-non-osi;branch=master;protocol=https"
SRCREV_edk2-non-osi    = "0320db977fb27e63424b0953a3020bb81c89e8f0"
SRCREV_edk2            = "3bf9a278b7d3ffdde0a40a039a9157f02989e6ba"
SRCREV_edk2-platforms  = "dc71a37b1bf6c0ffb3eb1d35dd2dcfb07fba5648"

EDK2_BUILD_RELEASE = "0"

EDK2_BIN_NAME      = "BL33_AP_UEFI.fd"
EDK2_ARCH          = "AARCH64"
EDK2_EXTRA_BUILD  += "-D ENABLE_MORELLO_CAP=1"
EDK_COMPILER       = "CLANGDWARF"

export CLANGDWARF_BIN            = "${LLVM_PATH}/"
export PACKAGES_PATH             = "${S}:${S}/edk2-platforms:${WORKDIR}/edk2-non-osi"

CC:remove      = "${CC_PURECAP_FLAGS}"
CXX:remove     = "${CC_PURECAP_FLAGS}"
LDFLAGS:remove = "${LD_PURECAP_FLAGS}"

do_deploy[noexec] = "1"

do_install() {
    install -d ${D}/firmware
    install ${B}/Build/${EDK2_PLATFORM}/${EDK2_BUILD_MODE}_${EDK_COMPILER}/FV/${EDK2_BIN_NAME} ${D}/firmware/uefi.bin
}