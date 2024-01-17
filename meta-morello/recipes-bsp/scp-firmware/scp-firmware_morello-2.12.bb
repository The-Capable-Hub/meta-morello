inherit cmake deploy

SUMMARY     = "SCP and MCP Firmware"
DESCRIPTION = "Firmware for SCP and MCP software reference implementation"
HOMEPAGE    = "https://github.com/ARM-software/SCP-firmware"

LICENSE          = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://license.md;beginline=5;md5=9db9e3d2fb8d9300a6c3d15101b19731 \
                    file://contrib/cmsis/git/LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

COMPATIBLE_MACHINE = "morello"
OUTPUTS_NAME       = "scp-firmware"
SECTION            = "firmware"

PACKAGE_ARCH       = "${MACHINE_ARCH}"

TOOLCHAIN          = "gcc"

MACHINE_SCP_REQUIRE ?= ""
MACHINE_SCP_REQUIRE:morello-fvp = "scp-firmware-morello-fvp.inc"
MACHINE_SCP_REQUIRE:morello-soc = "scp-firmware-morello-soc.inc"
require ${MACHINE_SCP_REQUIRE}

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS           += "virtual/arm-none-eabi-gcc-native virtual/board-firmware"
PROVIDES          += "virtual/${OUTPUTS_NAME}"

SRC_URI            = "gitsm://git.morello-project.org/morello/scp-firmware.git;protocol=https;branch=${SRCBRANCH}"
SRCREV             = "2713e710f25f6a0fa244cee2fde1d7fdcd78d8ab"
PV                 = "morello-2.12.0+git${SRCPV}"

SRCBRANCH          = "morello/release-1.7"

SCP_BUILD_RELEASE ?= "1"
SCP_COMPILER      ?= "arm-none-eabi"
SCP_PLATFORM      ?= "morello"
SCP_LOG_LEVEL     ?= "INFO"

SENSOR        = "${RECIPE_SYSROOT}/board-firmware/LIB/sensor.a"
B             = "${WORKDIR}/build/morello"
S             = "${WORKDIR}/git"

# Allow platform specific copying of only scp or both scp & mcp, default to both
FW_TARGETS ?= "scp mcp"
FW_INSTALL ?= "ramfw romfw"


LDFLAGS[unexport] = "1"


EXTRA_OEMAKE = "V=1 \
                BUILD_PATH='${B}' \
                PRODUCT='${SCP_PLATFORM}' \
                MODE='${SCP_BUILD_STR}' \
                LOG_LEVEL='${SCP_LOG_LEVEL}' \
                CC='${SCP_COMPILER}-gcc' \
                AR='${SCP_COMPILER}-ar' \
                SIZE='${SCP_COMPILER}-size' \
                OBJCOPY='${SCP_COMPILER}-objcopy' \
                "

do_configure[depends]  += "board-firmware:do_install"

FILES:${PN}   = "/firmware"
SYSROOT_DIRS += "/firmware"

FILES:${PN}-dbg      += "/firmware/*.elf"

# Skip QA check for relocations in .text of elf binaries
INSANE_SKIP:${PN}-dbg       = "arch textrel"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP       = "1"

do_configure() {

    cd ${S}

    for FW in ${FW_TARGETS}; do
        for TYPE in ${FW_INSTALL}; do

            local target="${FW}_${TYPE}"

            local extra_cmake="\
             -DSCP_ENABLE_DEBUGGER='0' \
             -DSCP_FIRMWARE_SOURCE_DIR:PATH='${SCP_PLATFORM}/${target}' \
             -DSCP_TOOLCHAIN:STRING='GNU' \
             -DDISABLE_CPPCHECK='1' \
             -DCMAKE_BUILD_TYPE=Release \
             "

            if [ "${target}" = "scp_ramfw_soc" ]; then
                extra_cmake="${extra_cmake} -DSCP_MORELLO_SENSOR_LIB_PATH='${SENSOR}'"
            fi

            local builddir="${B}/${target}"
            cmake -S ${S} -B ${builddir} ${extra_cmake}
        done
    done
}

do_compile() {
    cd ${S}
    for FW in ${FW_TARGETS}; do
        for TYPE in ${FW_INSTALL}; do
            local target="${FW}_${TYPE}"
            cmake --build "${B}/${target}"
        done
    done
}

do_install() {
    install -d ${D}/firmware
    for FW in ${FW_TARGETS}; do
        for TYPE in ${FW_INSTALL}; do
            local target="${FW}_${TYPE}"
            cp -rf "${B}/${target}/bin/"*.bin "${D}/firmware/${target}.bin"
        done
    done
}

do_deploy() {
    # Copy the images to deploy directory
    cp -rf ${D}/firmware/* ${DEPLOYDIR}/
}
addtask deploy after do_install