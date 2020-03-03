package com.blackfish.a1pedal.data

import com.google.gson.annotations.SerializedName
import com.prolificinteractive.materialcalendarview.CalendarDay

data class Request(
        val time: String,
        val date: String,
        @SerializedName("service_id")
        val serviceId: String,
        @SerializedName("driver_id")
        val driverId: String,
        val status: String
)

data class Response(
        val pk: Int,
        val createdBy: Int,
        val time: String,
        val date: String,
        val service: Info,
        val driver: Info,
        val status: String,
        val message: String
) {
        override fun equals(other: Any?): Boolean {
                if (other !is CalendarDay)
                        return false
                val (a, b, c) = date.split('/').map(String::toInt)
                return !(a != other.day || b != other.month || c != other.year)
        }
}

data class Info(
        val pk: String,
        val fio: String,
        val photo: String,
        val type: String,
        val name: String,
        @JvmField
        var phone: String,
        val brand: String,
        val model: String,
        val work: String,
        @SerializedName("last_activity")
        val lastActivity: String
)


data class UserInfo(
        val blocked: Boolean,
        val brand: Any,
        val discs: Any,
        val email: String,
        val fio: String,
        val gearbox: Any,
        val gps: String,
        @SerializedName("last_activity")
        val lastActivity: String,
        val miles: Any,
        val model: Any,
        @SerializedName("motor_volume")
        val motorVolume: Any,
        @SerializedName("motory_type")
        val motoryType: Any,
        val name: String,
        val number: Any,
        val phone: String,
        val photo: String,
        val pk: Int,
        val privod: Any,
        val site: String,
        val street: String,
        val timeFrom: String,
        val timeTo: String,
        val tire: Any,
        val type: String,
        val vin: Any,
        val weekends: String,
        val work: String,
        val year: Any,
        @SerializedName("you_blocked")
        val youBlocked: Boolean
)

data class UpdateInfo(
        val requests: Any,
        val friends: Friends
)

data class Friends(

        @SerializedName("user_friends")
        val userFriends: MutableList<Info>
)

data class UpdateRequest(
        val type: String,
        val phone: String
)

data class FriendRequest(
        val sender: Info,
        val recipient: Info
)

data class EventUpdate(
        @SerializedName("calendar_item_id")
        val calendarItemId: Int,
        val status: String
)

data class FriendsInfo(
        @JvmField
        val friends: Friends?,
        @JvmField
        var requests: MutableList<FriendRequest>?
) {
        operator fun get(position: Int): Info? {
                if (position < friends?.userFriends?.size ?: 0) {
                        return friends!!.userFriends[position]
                }
                return requests?.get(position - (friends?.userFriends?.size ?: 0))?.recipient
        }

        fun isRequest(position: Int) = position < friends?.userFriends?.size ?: 0

        fun size() = (friends?.userFriends?.size ?: 0) + (requests?.size ?: 0)

        fun remove(position: Int) {
                if (position < friends?.userFriends?.size ?: 0) {
                        friends!!.userFriends.removeAt(position)
                } else {
                        requests?.removeAt(position - (friends?.userFriends?.size ?: 0))
                }
        }

        fun makeFriend(position: Int) {
                val copy = requests!![position - (friends?.userFriends?.size ?: 0)]
                requests!!.removeAt(position - (friends?.userFriends?.size ?: 0))
                friends!!.userFriends.add(copy.recipient)
        }

        fun filter(check: (FriendRequest) -> Info) {
                requests =
                        requests?.filter { value -> !((friends?.userFriends?.contains(check(value))) ?: false) }?.toMutableList()

        }

        fun indexOf(id: String) = (friends?.userFriends?.indexOfFirst { it.pk == id } ?: -1)
}

