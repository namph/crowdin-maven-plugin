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
# Purpose : Prepare projects structure, clone projects if it is not existed, switch to expected version and update source code
#

# import projects declaration
source "../projects.sh"

# eXoProjects directory 
EXO_PROJECTS=`pwd`

length=${#projects[@]}

echo "=========================Preparing projects structure========================="
echo ""

for (( i=0;i<$length;i++)); do
  if [ -d $EXO_PROJECTS/${projects[${i}]}-${versions[${i}]} ]; then
    mv ${projects[${i}]}-${versions[${i}]} ${projects[${i}]}
    echo "Renamed ${projects[${i}]}-${versions[${i}]} to ${projects[${i}]}"
  fi
done

# Prepare the projects 
for (( i=0;i<$length;i++)); do
  echo ""
  echo "+++++++++++++++++++++++++Preparing the ${projects[${i}]} project+++++++++++++++++++++++"
  
  if [ ! -d $EXO_PROJECTS/${projects[${i}]} ]; then
    echo "--------------Cloning project from url: git@github.com:exoplatform/${projects[${i}]}.git---"
    git clone git@github.com:exoplatform/${projects[${i}]}.git
    echo "-------------------------Cloning done----------------------------------------"
  
    cd ${projects[${i}]}
    if [ "${projects[${i}]}" != "webos" ]; then
      git checkout origin/stable/${versions[${i}]} -b crowdin-stable-${versions[${i}]}
      echo "-------------------------Switched to origin/stable/${versions[${i}]}-------------"
    else
      git checkout origin/master -b crowdin-stable-${versions[${i}]}
      echo "-------------------------Switched to origin/master-------------"
    fi
    cd ..
  fi

  mv ${projects[${i}]} ${projects[${i}]}-${versions[${i}]}
  echo "-------------------------Renamed ${projects[${i}]} to ${projects[${i}]}-${versions[${i}]}-------------------"

  echo "+++++++++++++++++++++++++Preparing the ${projects[${i}]} project done++++++++++++++++++"
done

echo ""
echo "=========================Projects prepared===================================="