#!/bin/bash
export TARGET_PROJECTS=(
	":jar-searcher"
	":asm-tools"
	":jenkins-tools"
)

__gradle_exec(){
	./gradlew ${@};
}

__rp_deploy_project(){
	# execute deploying
	echo ">>> ${1} <<<" && __gradle_exec "${1}:release"
}

rp_deploy() {
   __gradle_exec ':base-tool-lib:clean' ':base-tool-lib:release'
   echo ":base-tool-lib:clean" ":base-tool-lib:release"
   for p in ${TARGET_PROJECTS[@]}; do __rp_deploy_project ${p}; done
}

rp_deploy