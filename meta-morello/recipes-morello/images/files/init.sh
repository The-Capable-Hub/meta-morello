#!/busybox-morello/busybox sh

mount() {
    /busybox-morello/busybox mount "$@"
}

umount() {
    /busybox-morello/busybox umount "$@"
}

grep() {
    /busybox-morello/busybox grep "$@"
}

cp() {
    /busybox-morello/busybox cp "$@"
}

mkdir() {
    /busybox-morello/busybox mkdir "$@"
}

switch_root () {
    /busybox-morello/busybox switch_root "$@"
}

sed () {
    /busybox-morello/busybox sed "$@"
}

echo "Running init script"

mkdir -m 0755 bin
mkdir -m 0755 include
mkdir -m 0755 share
mkdir -m 0755 proc
mkdir -m 0755 sys

cp /busybox-morello/busybox /bin/busybox

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