# Prototype for Exclusive execution

*Task:* it's needed to have ability to avoid duplicate method execution while current is in progress, wait for the end and return result.
Current prototype is a simple implementation using spring proxy to execute methods marked with annotation @Exclusive in such way.
