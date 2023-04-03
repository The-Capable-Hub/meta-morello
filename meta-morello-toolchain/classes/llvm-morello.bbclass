OVERRIDES =. "${@['', 'toolchain-${TOOLCHAIN}:']['${TOOLCHAIN}' != '']}"
OVERRIDES[vardepsexclude] += "TOOLCHAIN"

# var default init
LLVM_PATH    ??= ""
LLVM_VERSION ??= ""
LLVM_CONFIG  ??= ""
LIBCPLUSPLUS ??= ""
TC_DEPENDS   ??= ""

LLVM_VERSION:toolchain-llvm-morello     = "13.0.0"
LLVM_PATH:toolchain-llvm-morello        = "${STAGING_DIR_NATIVE}/usr/bin"

INHIBIT_DEFAULT_DEPS:toolchain-llvm-morello = "1"

TC_DEPENDS:append:toolchain-llvm-morello              = " virtual/llvm-morello-native"
TC_DEPENDS:append:toolchain-llvm-morello:class-target = " virtual/llvm-morello-runtime-native virtual/musl-morello"

DEPENDS:append:toolchain-llvm-morello = "${TC_DEPENDS}"

# leaving out ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} for now;
# -target is set kind off backwards inside the recipes until further notice
CC:toolchain-llvm-morello          = "${LLVM_PATH}/clang"
CXX:toolchain-llvm-morello         = "${LLVM_PATH}/clang++"
CPP:toolchain-llvm-morello         = "${LLVM_PATH}/clang -E"
CCLD:toolchain-llvm-morello        = "${LLVM_PATH}/clang"
HOSTCC:toolchain-llvm-morello      = "${LLVM_PATH}/clang"
RANLIB:toolchain-llvm-morello      = "${LLVM_PATH}/llvm-ranlib"
AR:toolchain-llvm-morello          = "${LLVM_PATH}/llvm-ar"
AS:toolchain-llvm-morello          = "${LLVM_PATH}/llvm-as"
NM:toolchain-llvm-morello          = "${LLVM_PATH}/llvm-nm"
OBJDUMP:toolchain-llvm-morello     = "${LLVM_PATH}/llvm-objdump"
OBJCOPY:toolchain-llvm-morello     = "${LLVM_PATH}/llvm-objcopy"
STRIP:toolchain-llvm-morello       = "${LLVM_PATH}/llvm-strip"
STRINGS:toolchain-llvm-morello     = "${LLVM_PATH}/llvm-strings"
READELF:toolchain-llvm-morello     = "${LLVM_PATH}/llvm-readelf"
LD:toolchain-llvm-morello          = "${LLVM_PATH}/ld.lld"
LTO:toolchain-llvm-morello         = "-fuse-ld=lld"
LLVM_CONFIG:toolchain-llvm-morello = "${LLVM_PATH}/llvm-config"

export ${LLVM_CONFIG}

DEPENDS:remove:toolchain-llvm-morello = "libgcc"
