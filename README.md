au.com.xhost.rightscale.api.client
==================================

RightScale Java API Client (unofficial).

Example usage:

```
package bar.foo.test;

import au.com.xhost.rightscale.api.client.*;

public class RightScaleAPITest {
    public static void main(String[] args) {
        RightScaleAPIClient rightscale;
        rightscale = new RightScaleAPIClient("rick@james.com", "charliemurphy", "1456", "1.5", "us-4.rightscale.com");
        rightscale.enableDebug();
        rightscale.login();
        System.out.print("Logged in "+rightscale.getUserName());
        System.out.print(rightscale.getRequest("/clouds"));
        System.out.print(rightscale.getServers());
    }
}
```

Use and run `client/src/main/java/au/com/xhost/rightscale/api/client/RightScaleAPIClientTestLocal.java` with your credentials for a quick way to test (this file is ignored by git).

License and Author
==================

Author:: Chris Fordham (<chris@fordham-nagy.id.au>)

Copyright 2013-2015, Chris Fordham

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
