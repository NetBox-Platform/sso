# NetboxSSO (Single sign-on by Netbox)
"Single sign-on SDK for using in third party apps, which provides login their users with Netbox"

The Single Sign-On (SSO) SDK is a software development kit designed by [Netbox](https://netbox.info/) team for third-party applications. It enables these apps to implement a seamless authentication experience for their users using 'netbox'. With this SDK, developers can integrate a secure and efficient login process, allowing users to access multiple applications without the need to repeatedly log in. This enhances user convenience and streamlines the authentication process across diverse third-party applications through the use of 'netbox' as the authentication provider.
In essence, our SDK allows users to either create a fully customized login button or utilize our pre-defined `LoginButton` with additional customization options, such as changing the text font and size, providing flexibility in adapting the authentication experience to their application's unique design.

Pre-defined LoginButtons:

 ![LoginButtons](https://github.com/NetBox-Platform/sso/assets/34549616/27fc5a73-bdc6-4d3b-b786-4cd6239b017a)

### Dependency

Step 1. Add jitpack repository

Legacy:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


New gradle:

    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
    			...
       			mavenCentral()
    			maven { url 'https://jitpack.io' }
        }
    }

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.NetBox-Platform:sso:0.0.3'
	}
	
### How to use
Full examples available in the links below:

[Sample1](https://github.com/NetBox-Platform/sso/blob/main/sample/src/main/java/ir/net_box/sso_sample/SampleActivity1.kt) (Using registerForActivityResult)

[Sample2](https://github.com/NetBox-Platform/sso/blob/main/sample/src/main/java/ir/net_box/sso_sample/SampleActivity2.kt) (Using startActivityForResult)
