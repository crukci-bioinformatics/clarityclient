## Process Automation

The Clarity Automation package provides a set of classes for running a process
through the Clarity API as if it were being run by a technician progressing the
process through the Clarity web pages. It is a general package to help programs that
need to run protocol steps automatically.

### Spring Configuration

The main class of the library, `ClarityProcessAutomation`, can be used
as a simple Java class (just create an instance) or can be injected into other
classes by Spring. There is a bean definition in `clarity-client-context.xml`
with the id "`clarityAutomation`".

### Basic Usage

The utility will help with moving a process through the screens one would otherwise
see in Clarity, waiting for the step to advance. Typically one would do the following
to run a protocol step from beginning to end.

#### Start the Process

The `beginProcessStep` method starts a process. It needs to be given a completed
[`StepCreation`](https://d10e8rzir0haj8.cloudfront.net/6.0/data_stp.html#step-creation)
object, which will provide the inputs to the process (which must be in the step's queue)
and the container type the outputs will be put into. If all is well, the call will return
a [`ProcessStep`](https://d10e8rzir0haj8.cloudfront.net/6.0/data_stp.html#step) object.

#### Wait for the Process to have started

When a process is started there may be EPP scripts triggered before the process can
be manipulated. It is typically EPP scripts that cause the wait between step changes.
One should call the `waitUntilProcessInState` method, which will poll Clarity
until the process moves into the state requested (or times out waiting for it). Client
code can then progress.

One has to be careful about which states a process goes through. Some steps begin with
the _Record Details_ screen, but others may go through other stages before that such as
_Placement_ or _Pooling_. The client code should know which step it is automating so the
author will know which stages the specific process type has.

#### Manipulate the Process

Once in the right state, you can use the API methods to update artifacts and the process
as you wish.

#### Move to the next stage

This is a repeat of starting the process: calling `waitUntilProcessInState` with
the next stage of the process as the status. One will need to do this for every stage
of the process until it is in the _Assign Next Steps_ state.

#### Completing the Process

When the process gets to _Assign Next Steps_ one has to fill in the `Actions` object
with what to do with the output artifacts. This is also protocol specific, as it might
be that the protocol finishes or there are one or more steps left to do. Sometimes there
may be a choice of next steps. This is step specific and can't be documented here save
to say that the choice is made by updating the `Actions` object for the process step.

### Automating EPP Execution

The utility also provides the method `waitUntilProgramCompletes` that performs a
similar "poll until finished" mechanism for EPP programs. These are the EPPs that would
be launched by a click on a button on the _Record Details_ screen rather than automatically
triggered by Clarity.

#### Start the EPP

The `startProgram` method starts an EPP. It needs to be given an `AvailableProgram`
object, a list of those available being held in the `ProcessStep` object.

#### Wait for the EPP to finish

The `waitUntilProgramCompletes` method should then be called. This is simpler than
its process equivalent as there are no steps to its progression, just a wait for the
EPP to finish. The `ProgramStatus` object returned from the `startProgram` call
is updated in place with information about the EPP's execution, and an exception thrown
if the EPP fails or if the time limit is exceeded for it to finish.
