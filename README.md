# Web-Tester   [ ![Download](https://api.bintray.com/packages/yanirta/generic/WebTester/images/download.svg) ](https://bintray.com/yanirta/generic/download_file?file_path=WebTester_0.1.2.jar)
Web-Tester is a Cli standalone tool to perform visual validation tests using Selenium and Applitools without coding.

If you don't have your Applitools account yet,
please sign up first at https://applitools.com/sign-up/ and get your Applitools api-key
that will be used next to execute the tests.

The tool supports three general modes:

1. Single - Validate single page based on provided url.
2. Interactive - Opens browser session to validated on demand, what's currently displayed.
3. Iterative - Automatically validate list of urls provided in sitemap.xml format.

To run in each one of the mode see the following sections.
Note that the Web-Tester was built in Java so every cli command should start with
>Java -jar WebTester.jar [mode] [mode specific parametes...]

## 1. Runnig Single mode
Made to easily validate single url without user intervention.

For example:
> Java -jar WebTester.jar -k [API_KEY] -pu https://applitools.com/resources

+ Required parametes:
    + `-k [api-key]` , Applitools api key
    + `-pu [url]` , Page url to validate
+ Optional paramaeters:
    + `-tn [name]`, Test name, otherwise will be derived from page-url
    + More optional parameters can be found in "[Shared parameters in all modes](#shared-parameters-in-all-modes)" section

##  2. Running Interactive mode
Provides on demand method to validate screenshot while performing manual testing.
Once executed, the tool will open the browser and wait for test/step name from the user,
which will trigger a visual validation of what's currently displayed.
The user will be able to drive the test and use
Applitools at any point to visually validate the page currently displayed.
The tool will continue to prompt for the next screens until the exit character ('~')
is inserted.

> Java -jar WebTester.jar Interactive -k [API_KEY] -bn MyBatch

+ Required parametes:
    + `-k [api-key]` , Applitools api key
    + `-st` , Use single test for all on-demand steps
    + `-ba [name]` , Batch name to aggregate all the tests
+ Optional parameters:
    + `-tn [name]` , Test name, if `-st` is set
    + More optional parameters can be found in "[Shared parameters in all modes](#shared-parameters-in-all-modes)" section

## 3. Running Iterative mode
Iterates over a defined set of urls automatically

> Java -jar WebTester.jar Iterate -k [API-KEY] -lo https://applitools.com/sitemap.xml

+ Required parametes:
    + `-k [api-key]` , Applitools api key
    + `-lo [url]` , The url to sitemap.xml file.
    Note that local path can be specified using the following syntax: file:///Users/yanir/mySitemaps/sitemap.xml
+ Optional parameters:
    + More optional parameters can be found in "[Shared parameters in all modes](#shared-parameters-in-all-modes)" section

## Shared parameters in all modes
+ `-br [browser]` - Set the browser to be used
+ `-vs [width x height]` - View port size to be achieved before validation
+ `-ba [name]` - Batch name
+ `-px [url]` - Proxy url for Applitools communication, for example: http://proxy.myorg.com:8080
+ `-ml [match-level]` - Match level, one of Strict, Layout, Layout2 or Content. Default: Strict
+ `-se [url]` - Selenium server/grid url
+ `-an [name]` - Application name
+ `-bn [name]` - Baseline environment name
+ `-ct [tob:bottom:left:right]` - Cut provider, avoid the specified amount of pixels from the bound of the screen.
IE, 100 pixels from the top will be given as `-ct 100,0,0,0`
+ `-as [url]` - Applitools server url
+ `-df` - Disable full page screenshot, will capture only the visible area of the page
+ `-us` - Use alternative method of scrolling when taking the screenshot
+ `-sb` - Show scrollbars, in some cases will capture the scrollbars as part of the screenshot
+ `-wb [seconds]` - Set wait before screenshot, will put sleep between scrolling and taking segment screenshot
+ `-cf [path]` - Custom desired capabilities file
+ `-sr [numeric-ratio]` - Overrides automatic ratio for troubleshooting and special cases.

## Other use cases

### Running Appium iOS mobile iterative test on Saucelabs

 > Java -jar WebTester.jar Iterate -k [API-KEY] -lo https://applitools.com/sitemap.xml
 -cf iphone7_sauce.json -se <http://USERNAME:ACCESSKEY@ondemand.saucelabs.com:80/wd/hub> -ct 65:0:0:0

provided that the content of 'iphone7_sauce.json' is:
```javascript
{
    "deviceName": "iPhone 7 Simulator",
    "platformVersion": "10.0",
    "platformName": "iOS",
    "browserName": "Safari"
}
```
Note: passing `-ct 65:0:0:0` argument will avoid 65 pixels from the top of the screen which are the status and the address bar of iPhone.

### Running Chrome emulation - Emulated devices in Desktop chrome
 >  Java -jar WebTester.jar Single -k [API-KEY] -pu http://www.asos.com/ 
 -br Chrome -cf mobile_emulation.json -sr 1"
 
 provided that the content of 'mobile_emulation.json' is:
 ```Javascript
 {
   "browserName": "chrome",
   "chromeOptions": {
     "mobileEmulation": {
       "deviceName": "Google Nexus 5"
     }
   },
   "version": "",
   "platform": "ANY"
 }
 ```
 
### Performing custom JavaScript code before validations
The WebTester supports execution of custom JavaScript code before every checkpoint.  
The tool expects for a file named 'execute_before_step.js' to be found in the execution folder.  
The file can contain any JavaScript syntax supported by the client web-page.  

Note that in most of the cases the logic will have to wait for elements to load before actually invoking the
 actions on them.
 
## Prerequisites
The integration with the different browsers is based on Selenium. To support local executions, the apropriate "driver"
should be unzipped and placed on the same folder of EyesUtilities.jar or to be installed put in the PATH variable.

+ [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/downloads)
+ [GeckoDriver](https://github.com/mozilla/geckodriver/releases) - Firefox driver
+ [IE Driver](https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver) - Follow the instructions to download only iedriverserver from the selenium repository.
 
 
 
