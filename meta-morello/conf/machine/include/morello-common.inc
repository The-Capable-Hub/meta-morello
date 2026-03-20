require ${COREBASE}/meta/conf/machine/include/arm/armv8-2a/tune-neoversen1.inc

MACHINEOVERRIDES =. "morello"

PREFERRED_VERSION_board-firmware     = "morello-1.8"
PREFERRED_VERSION_edk2-firmware      = "morello-2.7"
PREFERRED_VERSION_scp-firmware       = "morello-2.1%"
PREFERRED_VERSION_fiptool-native     = "2.7"

PREFERRED_VERSION_trusted-firmware-a = "morello-2.9"

PREFERRED_PROVIDER_virtual/kernel = "linux-morello"
PREFERRED_VERSION_virtual/kernel  = "morello-master"

PREFERRED_VERSION_musl            = "morello-1.9"

GDBVERSION                        = "morello-11.0"

MORELLO_TOOLCHAIN ??= "llvm-morello"

APP_DIR ?= "pure-cap-apps"

KERNEL_IMAGETYPE       = "Image"

# Having an image name suffix breaks the fvpconf handling
IMAGE_NAME_SUFFIX       = ""
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
