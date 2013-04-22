HTTPdLight
================================

A simple multi-threaded web server implemented in Java.
Methods Supported: GET, HEAD.
HTTPdLight does not support keep-alive or chunked transfer encoding.

Prerequisites:
-------------------------

The project is delivered as a Maven project. To build and execute you will need:

* JDK 1.5 or higher
* Maven
* For aditional dependecny information refer to pom.xml

Build Instructions:
-------------------------

Building an Executable Jar:
> mvn package

The excutable jar can be found at: ./target/httpdlight-1.0-SNAPSHOT.jar

Building API Documentation (javadocs):
> mvn site

The api documentation will be generated: ./target/site/project-reports.html
Additional project information will be available at: ./target/site/index.html

Usage:
-------------------------

Example Usage - Running as an executable jar:

> java -jar httpdlight-1.0-SNAPSHOT.jar 8080

Example Usage - Running as a library:

		HTTPdLight httpd = new HTTPdLight(port);
		HTTPdLight.initializeDefaultContent();
		new Thread(httpd).start();


License
-------------------------
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
