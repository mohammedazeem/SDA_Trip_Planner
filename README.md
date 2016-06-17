Automatic Trip Planner

Problem Statement
Everyday DC’s receive shipments to be delivered to customers. These shipments are scattered over an area of multiple localities and pincodes. Each DC has a certain number of SDAs to deliver them.

Everyday DC manager plans the trips based on the available SDA’s, DC Manager starts assigning the shipments based on personal heuristics to the SDA’s to deliver. The distribution of shipments to trips is highly dependent on the DC managers knowledge of the area and this process is time consuming when the DC starts getting many shipments in a day, The DC Manager roughly takes 30mins to plan a trip to a SDA. 
Solution
The solution is to automatically plan the trip recommendations for SDAs as accurately as possible and thus reduce this overhead time.

Goals
Automate the trip allocation process so that DC manager doesn’t have to spend any time on trip planning.
Create an optimum trip for each SDAs so as to reduce the mileage covered.
Minimize the mistakes that happen during manual assignment.
These will help reduce costs and time, and shift the focus completely on delivering shipments.
Allow DC Manager to override the trip plan recommendations.

MVP
From the given shipment addresses, create clusters automatically.
Provide the clusters to DC manager, so that he can make some necessary changes. This should reduce DC manager some amount of time in creating trips.

Assumptions
Address given by the customer is correct.
It is always possible to obtain nearby lat/long for a given address.
MVP will focus only on Forward/Deliveries.

Approach
Obtain the complete addresses of shipments, no. of shipments and no. of available SDAs for the day’s delivery.
Clean the address (Special characters etc.,)
Try to get geolocation for the given addresses. 
If not fruitful, clean the address and get the geo location.
If that also fails, try to get the geolocation of the locality given by user.
Try clustering the geolocations using one of algorithms (like k-means, k-medoids, nearest neighbors, etc)
Open source java libraries are available which offers clustering algorithms.
Output will be the clusters (number is same as number of SDAs available).
Based on the experiments, choose the best possible algorithm.
Integrate this to the SDA app, provide the manager with the clusters and allow for modifications.
