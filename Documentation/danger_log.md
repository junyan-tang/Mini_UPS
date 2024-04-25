# Danger Log

This document describes potential problems this web page might encounters under specific conditions.

## 1. High Concurrency

**Problem**: Users are unable to change the destination address of packages marked as "out for delivery," even when the package is still in the warehouse.

## 2. Duplicate Messages

**Problem**: Upon reconnection, the world resends the last message, leading to duplicate messages received by the UPS.

## 3. Stuck Shipment

**Problem**: If commands are not successfully sent to the world due to a closed connection, the shipment remains stuck in its current status.

## 4. Sending Confirmation Emails

**Problem**: Errors occur when sending confirmation emails to users who have not previously registered on the UPS website upon package arrival.

## 5. Repeated Sequence Number Issue in UPS Server

**Problem**: The UPS server occasionally sends repeated sequence numbers due to race conditions arising among multiple threads.

## 6. Package Destination Modification and Unintended Delivery Command Transmission

**Problem**: The delivery command is inadvertently sent to the world when a user attempts to change a package's destination, as the packageDao is not locked.