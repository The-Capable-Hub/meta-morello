
require recipes-kernel/linux/linux-yocto.inc
COMPATIBLE_MACHINE = "morello"
SUMMARY            = "Morello Linux kernel fork"
DESCRIPTION        = "CHERI aware Linux kernel"
OUTPUTS_NAME       = "morello-linux"
SECTION            = "kernel"

TOOLCHAIN          = "${MORELLO_TOOLCHAIN}"

DEPENDS                 += "rsync-native"
PROVIDES                += "virtual/${OUTPUTS_NAME}"

DEPENDS:remove:toolchain-llvm-morello        = "virtual/musl-morello"
RDEPENDS:${PN}:remove:toolchain-llvm-morello = " musl"

SRC_URI = " \
	git://git.morello-project.org/morello/kernel/linux;name=machine;protocol=https;nobranch=1  \
	git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};protocol=https \
"

PV  = "${LINUX_VERSION}+git${SRCPV}"
S   = "${WORKDIR}/git"
KMETA = "kernel-meta"

BUILD_CC  = "${LLVM_PATH}/clang"
BUILD_CPP = "${LLVM_PATH}/clang"
BUILD_CXX = "${LLVM_PATH}/clang++"
BUILD_LD  = "${LLVM_PATH}/ld.lld"

KERNEL_CC  = "${LLVM_PATH}/clang"
KERNEL_LD  = "${LLVM_PATH}/ld.lld"

ARCH = "arm64"

LINUX_KERNEL_TYPE     ?= "standard"

KERNEL_EXTRA_FEATURES ?= "features/net_sched/net_sched.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"

KBUILD_DEFCONFIG = "morello_transitional_pcuabi_defconfig"

KCONFIG_MODE = "--alldefconfig"

do_kernel_configcheck[noexec] = "1"
do_kernel_configme[depends]  += "llvm-morello-native:do_populate_sysroot"

FILES:${KERNEL_PACKAGE_NAME} += "${PURECAP_SYSROOT_DIR}${prefix}/src/linux-headers-morello/include"
SYSROOT_DIRS += "${PURECAP_SYSROOT_DIR}${prefix}/src/linux-headers-morello/include"

do_install:append() {
	install -d ${D}${PURECAP_SYSROOT_DIR}${prefix}/src/linux-headers-morello
	oe_runmake headers_install ARCH=arm64 INSTALL_HDR_PATH=${D}${PURECAP_SYSROOT_DIR}${prefix}/src/linux-headers-morello
}
