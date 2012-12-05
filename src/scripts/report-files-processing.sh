#!/bin/bash
sed -i '1i\<xmp>' ./report/translation_status.xml
sed -i '$ a\</xmp>' ./report/translation_status.xml
