

EXTRA_OECONF:append:morello-soc ="\ 
                --disable-efiemu \
                --enable-dependency-tracking \
                --disable-grub-themes \
                --disable-grub-mount \
                "

EXTRA_OECONF:append:morello-fvp ="\ 
                --disable-efiemu \
                --enable-dependency-tracking \
                --disable-grub-themes \
                --disable-grub-mount \
                "