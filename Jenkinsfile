/*
 * Copyright (C) 2018 Chikachi and other contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

pipeline {
  agent any
  options {
    timeout(time: 30, unit: 'MINUTES')
  }
  tools {
    jdk 'jdk_8u144'
  }
  stages {
    stage('Prepare') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew setupCiWorkspace clean spotlessApply'
      }
    }
    stage('Build') {
      steps {
        sh './gradlew build jar'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts 'build/libs/*.jar'
        fingerprint 'build/libs/*.jar'
      }
    }
  }
}