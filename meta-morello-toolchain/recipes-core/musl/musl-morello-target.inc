
COMPATIBLE_MACHINE = "morello"
SECTION            = "libs"

TOOLCHAIN          = "${MORELLO_TOOLCHAIN}"

MUSL_LDSO_ARCH = "aarch64_purecap"

DEBUG_PREFIX_MAP:remove = "-fcanon-prefix-map"

FILES:${PN}  = "${PURECAP_SYSROOT_DIR}"

FILES:${PN} += "${PURECAP_SYSROOT_DIR}${datadir} ${PURECAP_SYSROOT_DIR}${bindir} ${PURECAP_SYSROOT_DIR}${libdir}"
FILES:${PN} += "${PURECAP_SYSROOT_DIR}${includedir} ${PURECAP_SYSROOT_DIR}${includedir}/sys \
                 ${PURECAP_SYSROOT_DIR}${includedir}/arpa ${PURECAP_SYSROOT_DIR}${includedir}/bits \
                 ${PURECAP_SYSROOT_DIR}${includedir}/net ${PURECAP_SYSROOT_DIR}${includedir}/netinet \
                 ${PURECAP_SYSROOT_DIR}${includedir}/netpacket ${PURECAP_SYSROOT_DIR}${includedir}/arpa \
                 ${PURECAP_SYSROOT_DIR}${includedir}/scsi ${PURECAP_SYSROOT_DIR}${includedir}/sys"

FILES:${PN} += "${nonarch_base_libdir}/ld-musl-${MUSL_LDSO_ARCH}.so.1 ${sysconfdir}/ld-musl-${MUSL_LDSO_ARCH}.path"

FILES:${PN}-staticdev = "${PURECAP_SYSROOT_DIR}${libdir}/libc.a"
FILES:${PN}-dev =+ "${PURECAP_SYSROOT_DIR}${libdir}/libcrypt.a ${PURECAP_SYSROOT_DIR}${libdir}/libdl.a ${PURECAP_SYSROOT_DIR}${libdir}/libm.a \
                    ${PURECAP_SYSROOT_DIR}${libdir}/libpthread.a ${PURECAP_SYSROOT_DIR}${libdir}/libresolv.a \
                    ${PURECAP_SYSROOT_DIR}${libdir}/librt.a ${PURECAP_SYSROOT_DIR}${libdir}/libutil.a ${PURECAP_SYSROOT_DIR}${libdir}/libxnet.a \
                   "

SYSROOT_DIRS += "${sysconfdir} ${PURECAP_SYSROOT_DIR}${includedir}"

do_install() {

    echo "Installing into ${TARGET_INSTALL_DIR}"

    export CFLAGS=""

    install -d ${TARGET_INSTALL_DIR}${includedir} ${TARGET_INSTALL_DIR}${includedir}/sys ${TARGET_INSTALL_DIR}${includedir}/arpa \
               ${TARGET_INSTALL_DIR}${includedir}/bits ${TARGET_INSTALL_DIR}${includedir}/net ${TARGET_INSTALL_DIR}${includedir}/netinet \
               ${TARGET_INSTALL_DIR}${includedir}/netpacket ${TARGET_INSTALL_DIR}${includedir}/arpa ${TARGET_INSTALL_DIR}${includedir}/scsi \
               ${TARGET_INSTALL_DIR}${includedir}/sys

    local destdir="${TARGET_INSTALL_DIR}"

    local config="${CONFIGUREOPTS}"
    config="${config} --target=${ARCH_TRIPLE} ${EXTRA_CONFIGUREOPTS}"

	${S}/configure ${CONFIGUREOPTS}
	oe_runmake
	oe_runmake install DESTDIR="${destdir}"

    install -d ${TARGET_INSTALL_DIR}${sysconfdir}

    echo "${TARGET_INSTALL_DIR}${base_libdir}" > ${TARGET_INSTALL_DIR}${sysconfdir}/ld-musl-${MUSL_LDSO_ARCH}.path
    echo "${TARGET_INSTALL_DIR}${libdir}" >> ${TARGET_INSTALL_DIR}${sysconfdir}/ld-musl-${MUSL_LDSO_ARCH}.path
}