@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
	alias(opensavvyConventions.plugins.base)
	alias(opensavvyConventions.plugins.kotlin.application)
	alias(libsCommon.plugins.testBalloon)
}

kotlin {
	jvm {
		binaries {
			executable {
				mainClass = "opensavvy.telegram.tester.TestBotKt"
			}
		}
	}

	sourceSets.commonMain.dependencies {
		api(projects.sdk)
	}

	sourceSets.commonTest.dependencies {
		implementation(libsCommon.opensavvy.prepared.testBalloon)
	}
}
