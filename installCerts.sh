#!/bin/bash
keytool -import -alias ssu-ac-kr -noprompt -cacerts -storepass changeit -file ./src/jsMain/resources/ca/ssu-ac-kr.pem
keytool -import -alias digicert-global-root-ca -noprompt -cacerts -storepass changeit -file ./src/jsMain/resources/ca/digicert-global-root-ca.pem
keytool -import -alias thawte-rsa-ca -cacerts -noprompt -storepass changeit -file ./src/jsMain/resources/ca/thawte-rsa-ca.pem