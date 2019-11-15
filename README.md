# Future Battlegrounds

Future Battlegrounds (FB) is a networked spaceship combat engine server, using pseudonewtonian physics.

As it is merely a _combat engine_, this means that there is no inherent gameplay or UI.

Future Battlegrounds also serves as a playground to experiment with protocols, gameplay and APIs.

**NOTE: Future Battlegrounds is a work in progress. As such, parts of this readme may be outdated or not implemented yet!**

The author of Future Battlegrounds intends it to be used in a similar way as [RoboCode](https://robocode.sourceforge.io/), where users implement their own spaceship AIs to duke it out in battle. However, there should be no reason why Future Battlegrounds cannot be used to implement a game like [Star Control 2](https://en.wikipedia.org/wiki/Star_Control_II)'s Super Melee mode.

## Physics system

The physics system works similarly to the physics system of Star Control 2: spaceships have inertia in 2d space, but no rotational inertia. Furthermore, there is a hard speed cap, which depends on the ship. There is no friction; spaceships moving at a certain velocity will continue to move at that exact velocity as long as they don't use their thrusters.

Unlike Star Control 2, there is no spaceship-spaceship collision.

## Universe shape

The universe that is simulated is infinite - not a torus like in SC2.

To keep space battles centered on the universe origin, damage may be applied to spaceships that stray too far from the universe origin.

## Network APIs

Future Battlegrounds intends to support multiple network APIs, so as to make it easy to develop for using various languages. Currently, the following APIs are implemented or considered to be implemented:

- REST(ish)
- GraphQL
- GRPC

# Spaceship functionality

Spaceships function in a very similar way as they do in Star Control 2:

- Spaceships have hull (analogous to "crew" in SC2), which - when zero - means that the spaceship has been destroyed.
- Spaceships have a battery, which is recharged over time and has a max capacity. Most actions, other than rotating or thrusting, use up battery.
- Spaceships generally only have forward thrust, no reverse thrust.
- Spaceships generally have boolean thrusters (on or off).

# General API documentation

In general, the APIs work as follows:

## Units

- All temporal units are _seconds since simulation start_.
- All spatial units are _2d coordinates_ of an unspecified unit, usually represented as a vector with an x and y component.
- All velocities are specified as a vector with an x and y component, which specifies the rate of movement for 1 second.

## Spaceship operation

- When you construct a spaceship, you get a UUID back. This is the only time you are ever able to get a spaceship's UUID, so UUIDs should be treated as passwords or API keys.
- Whenever you submit a command, you need to supply the ship UUID.
- Ships are automatically destroyed if no command has been received for approximately 60 seconds.
  - you can use a 'keepalive' command - which is a no-op - to keep a ship alive.
- Commands are generally submitted in the (near) future, and have two parameters:
  - the _activation time_ specifies when the command should be invoked.
  - the _duration time_ specifies.
- Commands are _additive_, meaning that if you send the following command list (either in a single request for protocols that support it, or multiple requests):
  ```
  At 0s, thrust for 2 seconds
  At 0.5s, rotate left for 1 second
  ```
  the spaceship will thrust from 0s to 2s, and rotate left from 0.5s to 1s.
- Streaming endpoints should emit new data at a fixed tick rate, but this cannot be guaranteed. Every time you receive data, the data contains the simulation time.
- If a command has a start/stop time that falls within a simulation tick, it may be extended or changed such that it falls exactly within a single timestap.

# Collision detection

Collision detection between bullets and spaceships will be implemented as a simple point-circle check at first (is the bullet position within X coordinates of a spaceship's center position?), however the intention is to extend it to line-circle (does the line segment describing the bullet trajectory for this timestep collide with a circle of X coordinates in size centered on the spaceship's origin) and eventually to line-line (does the line segment describing the bullet trajectory for this timestep collide with any of the line segments describing the spaceship's polygonal shape).

# IFF

To allow various games and gameplay modes to be implemented, whenever you spawn a spaceship you may declare an IFF ("Identify Friend and Foe") string. Your weapons will not damage or interact with spaceships with the same IFF as you have. If you declared no IFF when spawning your ship, your IFF will be automatically set and be unique to your ship.

# Future (heh) work

The following things are considered, and may be specifoed, elaborated on or implemented in the future:

- Server Federation
- Autoscaling and migration of servers based on spatial subdivision (quad trees? sectors?)
- Restricted visibility; sonar?
- Gravity wells
- Custom ships (custom polygons, ships assembled from a set of weapons and components)
- Public chat, IFF chat
- Objects with collision detection and optional physics
- Spatial annotations (allow clients to draw lines and polygons on the battlefield, both publicly and within an IFF, which cannot be interacted with)
- "God Mode": allow clients using a godmode secret token to warp ships and objects anywhere
