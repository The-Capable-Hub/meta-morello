
COMPATIBLE_MACHINE = "morello"
DESCRIPTION        = "The Morello LLVM Compiler Infrastructure for A64"
SUMMARY            = "LLVM Morello"
HOMEPAGE           = "https://git.morello-project.org/morello/llvm-project-releases"
LICENSE            = "Apache-2.0-with-LLVM-exception"

RPROVIDES:${PN}    = "llvm-morello"
RDEPENDS:${PN}     = "zlib"
SRCBRANCH          = "morello/linux-aarch64-release-1.8"

SRC_URI = "https://git.morello-project.org/morello/llvm-project-releases/-/archive/${SRCBRANCH}/llvm-project-releases-morello-linux-aarch64-release-1.8.tar.gz"

SRC_URI[sha256sum] = "ea43c6e72eb00c516223ec7f318cdd17a461eae4a27250b3da74cdf0e390390c"
LIC_FILES_CHKSUM = "file://include/llvm/Support/LICENSE.TXT;md5=2524adb3fbc86d9bb9443d92f4b63013"

S = "${WORKDIR}/llvm-project-releases-morello-linux-aarch64-release-1.8"

FILES:${PN} += "${bindir}"
FILES:${PN} += "${libdir} ${libdir}/clang"
FILES:${PN} += "${includedir}"

FILES:${PN}-staticdev += "${libdir}/*.a ${libdir}/clang/14.0.0/lib/linux/*.a \
                          ${libdir}/clang/14.0.0/lib/aarch64-unknown-linux-musl_purecap/*.a \
                          ${libdir}/clang/14.0.0/lib/aarch64-unknown-linux-gnu/*.a \
                          "
FILES:${PN}-dev += "\
  ${datadir} \
  ${libdir}/cmake \
"

FILES:libclang = "\
  ${libdir}/libclang.so.${PV} \
"

FILES:lldb = "\
  ${bindir}/lldb \
"

FILES:lldb-server = "\
  ${bindir}/lldb-server \
"

FILES:liblldb = "\
  ${libdir}/liblldbIntelFeatures.so* \
  ${libdir}/liblldb.so* \
"

INSANE_SKIP_${PN}           = "staticdev"
INHIBIT_SYSROOT_STRIP       = "1"
INHIBIT_PACKAGE_STRIP       = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

do_install() {
    install -d ${D}${prefix}  ${D}${libdir} ${D}${bindir} ${D}${includedir} ${D}${datadir} ${D}${libexecdir}
    cp -rf ${S}/lib/* ${D}${libdir}
    cp -rf ${S}/bin/* ${D}${bindir}
    cp -rf ${S}/include/* ${D}${includedir}
    cp -rf ${S}/share/* ${D}${datadir}
    cp -rf ${S}/libexec/* ${D}${libexecdir}
}