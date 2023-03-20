

OUTPUTS_NAME       = "grub-efi"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

GRUB_BUILDIN:morello-soc = " boot chain configfile ext2 fat gzio help linux loadenv \
    lsefi normal ntfs ntfscomp part_gpt part_msdos progress read search \
    search_fs_file search_fs_uuid search_label terminal terminfo \
    "

GRUB_BUILDIN:morello-fvp = " boot chain configfile ext2 fat gzio help linux loadenv \
    lsefi normal ntfs ntfscomp part_gpt part_msdos progress read search \
    search_fs_file search_fs_uuid search_label terminal terminfo \
    "

SRC_URI:append:morello-soc = " file://files/grub-config.cfg"
SRC_URI:append:morello-fvp = " file://files/grub-config.cfg"

do_deploy:append:morello-soc() {
    install -d ${DEPLOYDIR}/${OUTPUTS_NAME}
    install -m 644 ${B}/${GRUB_IMAGE_PREFIX}${GRUB_IMAGE} "${DEPLOYDIR}/${OUTPUTS_NAME}/"
    install -m 644 ${WORKDIR}/files/grub-config.cfg "${DEPLOYDIR}/${OUTPUTS_NAME}/grub-config.cfg"
}

do_deploy:append:morello-fvp() {
    install -d ${DEPLOYDIR}/${OUTPUTS_NAME}
    install -m 644 ${B}/${GRUB_IMAGE_PREFIX}${GRUB_IMAGE} "${DEPLOYDIR}/${OUTPUTS_NAME}/"
    install -m 644 ${WORKDIR}/files/grub-config.cfg "${DEPLOYDIR}/${OUTPUTS_NAME}/grub-config.cfg"
}
