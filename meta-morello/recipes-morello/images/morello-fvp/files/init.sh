#!/bin/busybox sh

mount() {
    /bin/busybox mount "$@"
}

umount() {
    /bin/busybox umount "$@"
}

grep() {
    /bin/busybox grep "$@"
}

cp() {
    /bin/busybox cp "$@"
}

mkdir() {
    /bin/busybox mkdir "$@"
}

switch_root () {
    /bin/busybox switch_root "$@"
}

sed () {
    /bin/busybox sed "$@"
}

echo "Running init script"

mount -t proc proc /proc
grep -qE $'\t'"devtmpfs$" /proc/filesystems && mount -t devtmpfs dev /dev
mount -t sysfs sysfs /sys

echo "Installing busybox..."

/bin/busybox --install -s

! grep -qE $'\t'"devtmpfs$" /proc/filesystems && mdev -s

echo "/bin/sh as PID 1!"
echo "init.sh"
exec setsid cttyhack sh
echo setsid ctty hack failed so \"exec /bin/sh\" fallback will be used
exec /bin/sh