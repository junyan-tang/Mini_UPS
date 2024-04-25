# Danger Log

This document describes potential problems this web page might encounters under specific conditions.

## 1. High Concurrency

**Problem**: Users are unable to change the destination address of packages marked as "out for delivery," even when the package is still in the warehouse.

**Solution**: Implement an additional check upon user submission to verify that the package has not yet been dispatched for delivery.

## 2. Duplicate Messages

**Problem**: Upon reconnection, the world resends the last message, leading to duplicate messages received by the UPS.

**Solution**: Examine the package status for the specific package ID to determine if it has already been delivered. If so, do not send confirmation emails to the user or a "Package Arrived" message to Amazon.

## 3. Stuck Shipment

**Problem**: If commands are not successfully sent to the world due to a closed connection, the shipment remains stuck in its current status.

**Solution**: Introduce an acknowledgement confirmation mechanism that resends missed messages if the UPS does not receive an acknowledgement from the world after a certain amount of time.

## 4. Sending Confirmation Emails

**Problem**: Errors occur when sending confirmation emails to users who have not previously registered on the UPS website upon package arrival.

**Solution**: Search the user database to determine if the username exists. If it does not, skip the function of sending a confirmation email.

## 5. Repeated Sequence Number Issue in UPS Server

**Problem**: The UPS server occasionally sends repeated sequence numbers due to race conditions arising among multiple threads.

**Solution**: Implement AtomicLong to ensure that sequence numbers are generated in a thread-safe manner, eliminating the risk of race conditions.

## 6. Package Destination Modification and Unintended Delivery Command Transmission

**Problem**: The delivery command is inadvertently sent to the world when a user attempts to change a package's destination, as the packageDao is not locked.

**Solution**: Implement pessimistic locking on the row to be modified, ensuring secure access and modification of the package's information and preventing unauthorized delivery commands from being sent.