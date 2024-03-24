package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.sql.Table

object AdventureInvitesTable : Table("adventure_invites") {
    val adventureId = integer("adventure_id")
    val invitedUser = long("invited_user")
}
