## In this project, you will implement in Java a Reliable Data Transfer protocol (RDT).  RDT provides reliability and flow control using the Automatic Repeat Request (ARQ) and Sliding Window protocols.
RDT is supposed to be implemented in the OS Kernel at the Data Link Layer.

However, that being too complex, let us implement RDT as a transport-layer File Transfer service over UDP, using the Sliding Window concepts we have learnt in the context of the Data Link Layer. 

[adapted from http://nsl.cs.sfu.ca/teaching/09/371/prj3_reliableTransferProtocol.html]

# Objective
> To implement a File Transfer service in Java using ARQ Protocols:
>> Stop-and-Wait
>> Go-Back-N
>> Selective Repeat