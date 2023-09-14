# Prototype for Exclusive execution

A simple lib to show Spring autoconfiguration and starters using.

*Task:* it's needed to have ability to avoid duplicate method execution while current is in progress, wait for the end and return result.
Current prototype is a simple implementation using Spring proxy to execute methods marked with annotation @Exclusive in such way.
