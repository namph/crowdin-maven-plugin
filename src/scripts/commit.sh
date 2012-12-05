#!/bin/bash
#
# Copyright (C) 2003-2012 eXo Platform SAS.
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU Affero General Public License
# as published by the Free Software Foundation; either version 3
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, see<http://www.gnu.org/licenses/>.#
#
#
# Purpose : this script is to be called from jenkins to commit new translations into source code
# If there are new translations, a new branch will be created. If no, no branch created
#

# import projects declaration
echo `pwd`
source "../config/projects.sh"
rm ./report/commit_report.txt
touch ./report/commit_report.txt
cd ./eXoProjects/


# eXoProjects directory 
EXO_PROJECTS=`pwd`
length=${#projects[@]}
date=$(date +"%Y-%m-%d")

for (( i=0;i<$length;i++)); 
do
    cd ${projects[${i}]}
    branch_name="crowdin/${projects[${i}]}-${versions[${i}]}/$date"
    #check if there are new translations or not 
    RUN=0
    git diff --no-ext-diff --quiet --exit-code || RUN=1
    if [ $RUN = 1 ]; then
    git remote add exodev git@github.com:exodev/${projects[${i}]}.git
    git fetch exodev
    git add --all
    git commit -m "Crowdin-$date : Update translations from crowdin to source code."
    git push exodev crowdin-stable-${versions[${i}]}:$branch_name
    echo -e "$branch_name\n" >> ../../report/commit_report.txt
    fi
    if [ $RUN = 0 ]; then
    echo "INFO: No translations have been updated into source code of '${projects[${i}]}-${versions[${i}]}'"
    cd ..
fi
done


