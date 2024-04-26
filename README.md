# UPS-AMAZON WORLD
Xueyi Fu, Junyan Tang (UPS)  Ritik Agrawal, Jones Larry (Amazon)  
Zixu Geng, Haolou Sun (UPS)  Xianjing Huang, Shuqi Shen (Amazon)
           
Since we won’t have access to real warehouses and trucks, our code will interact with a simulated world provided. We connect to the simulation server (port 12345 for UPS, port 23456 for Amazon), and send commands and receive notifications.

For Amazon, we have a web interface on which someone can buy a product. We then go through all the steps to get the package delivered.
 
For UPS, we have a web interface which will display the shipments that exist, and their status (e.g. created, truck en route to warehouse, truck waiting for package, out for delivery).