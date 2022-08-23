# Introduction

The goal of this project is to run Kafka Connect in a more cloud native approach. Instead of using 
Kafka Connect worker nodes we will use Kubernetes as the scheduler. Each connector and corresponding tasks
will be isolated in their own pod. This should allow for a better scheduling experience.

## Installation

