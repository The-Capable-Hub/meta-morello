require ${COREBASE}/meta/conf/machine/include/arm/armv8-2a/tune-neoversen1.inc

MACHINEOVERRIDES =. "morello"

PREFERRED_PROVIDER_virtual/kernel = "linux-morello"
PREFERRED_VERSION_virtual/kernel = "1.5"

LLVM_SHARED_SOURCE ?= "${TMPDIR}/work-shared/${MACHINE}/llvm-morello-source"

MUSL_INSTALL_DIR ??=""

C64_ARCH_TRIPLE = "aarch64-linux-musl_purecap"
A64_ARCH_TRIPLE = "aarch64-linux-gnu"

A64_LIB_TRIPLE = "aarch64-unknown-linux-gnu"
C64_LIB_TRIPLE = "aarch64-unknown-linux-musl_purecap"

A64_ARCH_FLAGS = "-march=armv8"
C64_ARCH_FLAGS = "-march=morello+c64 -mabi=purecap"

MORELLO_ARCH       ?= "c64"
GLOBAL_ARCH_TRIPLE ?= "${C64_ARCH_TRIPLE}"
GLOBAL_LIB_TRIPLE  ?= "${C64_LIB_TRIPLE}"
GLOBAL_ARCH_FLAGS  ?= "${C64_ARCH_FLAGS}"

APP_DIR ?= "pure-cap-apps"