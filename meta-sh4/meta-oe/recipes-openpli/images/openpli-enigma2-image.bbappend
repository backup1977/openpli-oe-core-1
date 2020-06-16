WIFI_DRIVERS_remove_sh4 = "\
	firmware-carl9170 \
	firmware-htc7010 \
	firmware-htc9271 \
	firmware-rt73 \
	firmware-rtl8712u \
	\
	kernel-module-ath9k-htc \
	kernel-module-carl9170 \
	kernel-module-r8712u \
	kernel-module-rt2500usb \
	kernel-module-rt2800usb \
	kernel-module-rt73usb \
	kernel-module-rtl8192cu \
	"

WIFI_BSP_DRIVERS_append_sh4= "\
	firmware-mt7601u \
	firmware-rtl8192eu \
	\
	kernel-module-8192eu \
	kernel-module-mt7601usta \
	kernel-module-rt5572sta \
	"

IMAGE_INSTALL_append_sh4= "\
	enigma2-plugin-skins-simple-gray-hd \
	"
