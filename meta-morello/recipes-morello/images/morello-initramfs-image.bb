inherit core-image

DESCRIPTION        = "Small image with purecap dev environment and minimal bloat."
LICENSE            = "MIT"

INITRAMFS_SCRIPTS ?= "\
                      morello-initramfs-scripts \
                     "

PACKAGE_INSTALL  = "${INITRAMFS_SCRIPTS}"
PACKAGE_INSTALL += "musl-libc busybox-morello pure-cap-app llvm-morello gdbserver gdb llvm-morello-staticdev"

IMAGE_FEATURES = ""

export IMAGE_BASENAME = "initramfs"
IMAGE_NAME_SUFFIX    ?= ""
IMAGE_LINGUAS         = ""

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

INITRAMFS_MAXSIZE = "1280000"

IMAGE_ROOTFS_SIZE        = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|aarch64.*)-(linux.*|freebsd.*)'
