FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "libxml2"

PACKAGECONFIG_append = " libass libbluray libfreetype librtmp libvorbis \
                        mp3lame openjpeg openssl vpx wavpack x265"

PACKAGECONFIG[libass] = "--enable-libass,--disable-libass,libass"
PACKAGECONFIG[libbluray] = "--enable-libbluray --enable-protocol=bluray,--disable-libbluray,libbluray"
PACKAGECONFIG[libfreetype] = "--enable-libfreetype,--disable-libfreetype,freetype"
PACKAGECONFIG[librtmp] = "--enable-librtmp,--disable-librtmp,librtmp rtmpdump"
PACKAGECONFIG[openjpeg] = "--enable-libopenjpeg,--disable-libopenjpeg,openjpeg"
PACKAGECONFIG[wavpack] = "--enable-libwavpack,--disable-libwavpack,wavpack"
PACKAGECONFIG[x265] = "--enable-libx265,--disable-libx265,x265"
PACKAGECONFIG[libv4l2] = "--enable-libv4l2,--disable-libv4l2,v4l-utils"

MIPSFPU = "${@bb.utils.contains('TARGET_FPU', 'soft', '--disable-mipsfpu', '--enable-mipsfpu', d)}"

SRC_URI_append += " \
	file://4_02_fix_mpegts.patch \
	file://4_10_rtsp_patch \
	file://4_11_dxva2_patch \
	"

EXTRA_FFCONF_arm = " \
    --prefix=${prefix} \
    --disable-static \
    --disable-runtime-cpudetect \
    --enable-ffprobe \
    --disable-altivec \
    --disable-amd3dnow \
    --disable-amd3dnowext \
    --disable-mmx \
    --disable-mmxext \
    --disable-sse \
    --disable-sse2 \
    --disable-sse3 \
    --disable-ssse3 \
    --disable-sse4 \
    --disable-sse42 \
    --disable-avx \
    --disable-xop \
    --disable-fma3 \
    --disable-fma4 \
    --disable-avx2 \
    --enable-inline-asm \
    --enable-asm \
    --disable-x86asm \
    --disable-fast-unaligned \
    --enable-muxers \
    --enable-encoders \
    --enable-decoders \
    --disable-decoder=truehd \
    --disable-decoder=mlp \
    --enable-demuxers \
    --enable-parsers \
    --enable-bsfs \
    --enable-protocols \
    --enable-indevs \
    --enable-outdevs \
    --enable-filters \
    --disable-doc \
    --enable-libfdk-aac \
    --enable-encoder=libfdk_aac \
    --disable-htmlpages \
    --disable-manpages \
    --disable-podpages \
    --disable-txtpages \
    --disable-debug \
    --enable-zlib \
    ${@bb.utils.contains("TARGET_ARCH", "mipsel", "${MIPSFPU} --disable-vfp --disable-neon --disable-mipsdsp --disable-mipsdspr2", "", d)} \
    ${@bb.utils.contains("TARGET_ARCH", "arm", "--enable-armv6 --enable-armv6t2 --enable-vfp --enable-neon", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "aarch64", "--enable-armv8 --enable-vfp --enable-neon", "", d)} \
    --extra-cflags="${TARGET_CFLAGS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -ffunction-sections -fdata-sections -fno-aggressive-loop-optimizations" \
    --extra-ldflags="${TARGET_LDFLAGS},--gc-sections -Wl,--print-gc-sections,-lrt" \
"

EXTRA_FFCONF_mipsel = " \
    --prefix=${prefix} \
    --disable-static \
    --disable-runtime-cpudetect \
    --disable-programs \
    --disable-altivec \
    --disable-amd3dnow \
    --disable-amd3dnowext \
    --disable-mmx \
    --disable-mmxext \
    --disable-sse \
    --disable-sse2 \
    --disable-sse3 \
    --disable-ssse3 \
    --disable-sse4 \
    --disable-sse42 \
    --disable-avx \
    --disable-xop \
    --disable-fma3 \
    --disable-fma4 \
    --disable-avx2 \
    --enable-inline-asm \
    --enable-asm \
    --disable-x86asm \
    --disable-fast-unaligned \
    --disable-muxers \
    --disable-encoders \
    --disable-decoders \
    --enable-decoder=wmalossless \
    --enable-decoder=wmapro \
    --enable-decoder=wmav1 \
    --enable-decoder=wmav2 \
    --enable-decoder=wmavoice \
    --enable-decoder=dca \
    --disable-demuxers \
    --disable-parsers \
    --disable-bsfs \
    --disable-protocols \
    --disable-devices \
    --disable-filters \
    --disable-doc \
    --disable-htmlpages \
    --disable-manpages \
    --disable-podpages \
    --disable-txtpages \
    --disable-debug \
    --disable-zlib \
    ${@bb.utils.contains("TARGET_ARCH", "mipsel", "${MIPSFPU} --disable-vfp --disable-neon --disable-mipsdsp --disable-mipsdspr2", "", d)} \
    --extra-cflags="${TARGET_CFLAGS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -ffunction-sections -fdata-sections -fno-aggressive-loop-optimizations" \
    --extra-ldflags="${TARGET_LDFLAGS},--gc-sections -Wl,--print-gc-sections,-lrt" \
"
