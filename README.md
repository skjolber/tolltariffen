[![Build Status](https://travis-ci.org/skjolber/tolltariffen.svg)](https://travis-ci.org/skjolber/tolltariffen)

# tolltariffen

This library hosts a few converters and data files for the Norwegian customs codes:

 * 2019
     * English, Norwegian
     * Single and full depth. 

The original list is available in PDF and Excel, which are really not very well suited for my (and most people's) applications. So the input Excel file is converted to various JSON files.

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].
 
## Obtain
The project is implemented in Java and built using [Maven]. The project is available on the central Maven repository.

Example dependency config:

```xml
<dependency>
    <groupId>com.github.skjolber.tolltariffen</groupId>
    <artifactId>data</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

# Usage
Get the files by downloading them directly from this repository, or use the published artifact.

 * full depth (about 750k)
     * tolltariffen/2019/tolltariffen-en.json
     * tolltariffen/2019/tolltariffen-no.json
 * single depth (about 170k)
      * tolltariffen/2019/tolltariffen-en-1.json
      * tolltariffen/2019/tolltariffen-no-1.json

```java
InputStream is = ..;
ToolTariffen tollTariffen = TollTariffenBuilder.newBuilder().withInput(is)).build();
		
String search = tollTariffen.searchValue("01.01.3000");
```

for loading just a subset of the codes, do with

```java
ToolTariffen tollTariffen = TollTariffenBuilder.newBuilder().withCodes("01.01").withInput(is).build();
```

where only top-level codes are supported.

## Details
The data structure is 'interesting', with some subcategories not having codes, only descriptions. Basically codes without subcodes are normalized so that they are stored with a single key-value;

```json
  "02.05.0000" : "Meat of horses, asses, mules or hinnies, fresh, chilled or frozen.",

```
whereas a code which also has subcodes is stored as

```json
{
    "code" : "53.09",
    "codes" : [ array of children ],
    "desc" : "Woven fabrics of flax."
}
```

where the `code` field is optional. A simple `TollTariffen` tools is available for searching the files.

Use the `TollTariffenTool` in the `generator` project to regenerate the files (this is a manual operation for now). 

# Get involved
If you have any questions, comments or improvement suggestions, please file an issue or submit a pull-request.

## License
[Apache 2.0]

# History
 - 1.0.0: Initial drop

[Apache 2.0]: 			http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:			https://github.com/skjolber/tolltariffen/issues
[Maven]:					http://maven.apache.org/
