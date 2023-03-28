require ${COREBASE}/meta/conf/machine/include/arm/armv8-2a/tune-neoversen1.inc

MACHINEOVERRIDES =. "morello"

PREFERRED_VERSION_board-firmware     = "morello-1.4"
PREFERRED_VERSION_edk2-firmware      = "morello-2.4"
PREFERRED_VERSION_scp-firmware       = "morello-2.1%"

PREFERRED_VERSION_trusted-firmware-a = "morello-2.7"

PREFERRED_PROVIDER_virtual/kernel = "linux-morello"
PREFERRED_VERSION_virtual/kernel  = "morello-1.5"

PREFERRED_VERSION_musl            = "morello-1.5"

GDBVERSION                        = "morello-11.0"

MUSL_INSTALL_DIR ??=""
MORELLO_TOOLCHAIN ??= "llvm-morello"

C64_ARCH_TRIPLE = "aarch64-linux-musl_purecap"
A64_ARCH_TRIPLE = "aarch64-linux-gnu"

A64_LIB_TRIPLE = "aarch64-unknown-linux-gnu"
C64_LIB_TRIPLE = "aarch64-unknown-linux-musl_purecap"

A64_ARCH_FLAGS = "-march=armv8"
C64_ARCH_FLAGS = "-march=morello+c64 -mabi=purecap"

GLOBAL_ARCH_TRIPLE ?= "${C64_ARCH_TRIPLE}"
GLOBAL_LIB_TRIPLE  ?= "${C64_LIB_TRIPLE}"
GLOBAL_ARCH_FLAGS  ?= "${C64_ARCH_FLAGS}"

APP_DIR ?= "pure-cap-apps"

KERNEL_IMAGETYPE       = "Image"

IMAGE_FSTYPES          += "wic"

WKS_FILE               ?= "morello-efidisk.wks"
WKS_FILE_DEPENDS:append = " ${EXTRA_IMAGEDEPENDS}"

EXTRA_IMAGEDEPENDS     += "virtual/scp-firmware"
EXTRA_IMAGEDEPENDS     += "virtual/trusted-firmware-a"
EXTRA_IMAGEDEPENDS     += "virtual/edk2-firmware"
EXTRA_IMAGEDEPENDS     += "board-firmware-image"

EFI_PROVIDER           ?= "grub-efi"
MACHINE_FEATURES       += "efi"

INITRAMFS_MAXSIZE      = "640000"
INITRAMFS_IMAGE        = "morello-initramfs-image"
INITRAMFS_IMAGE_BUNDLE = "0"
