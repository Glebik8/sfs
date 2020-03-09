package com.blackfish.a1pedal.data

import android.os.Parcelable
import com.blackfish.a1pedal.ProfileInfo.User
import com.google.gson.annotations.SerializedName
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize

data class Request(
        val time: String,
        val date: String,
        @SerializedName("service_id")
        val serviceId: String,
        @SerializedName("driver_id")
        val driverId: String,
        val status: String
)

@Parcelize
data class Response(
        val pk: Int,
        val createdBy: Int,
        val time: String,
        val date: String,
        val service: Info,
        val driver: Info,
        val status: String,
        val message: String
) : Parcelable {

        val isNew
                get() = status == "new"
        val name
                get() = if (User.getInstance().isDriver) service.name else driver.name

        val notMe
                get() = if (User.getInstance().isDriver) service else driver

        override fun equals(other: Any?): Boolean {
                if (other !is CalendarDay)
                        return false
                val (a, b, c) = date.split('/').map(String::toInt)
                return !(a != other.day || b != other.month || c != other.year)
        }

}

@Parcelize
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
) : Parcelable


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
        @JvmField
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
) {
        val notMe
                get() = if (User.getInstance().isDriver) recipient else sender
}

data class EventUpdate(
        @SerializedName("calendar_item_id")
        val calendarItemId: Int,
        val status: String

)
class ArchiveEvent(
        val content: String,
        val event: List<Response>
): ExpandableGroup<Response>(content, event)


data class MessageRequest(
        val sender: Int,
        val recipient: Int,
        val type: String,
        @SerializedName("chat_id")
        val chatId: Int,
        val content: String
)

data class DeleteRequest(
        @SerializedName("calendar_item_id")
        val calendarItemId: Int
)

data class StatusRequest(
        val status: String,
        val msg: String
)

data class CreateDialogRequest(
        val sender: Int,
        val recipient: Int,
        val type: String,
        val content: String
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
                return requests?.get(position - (friends?.userFriends?.size ?: 0))?.notMe
        }

        fun isFriend(position: Int) = position < friends?.userFriends?.size ?: 0

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
                friends!!.userFriends.add(copy.notMe)
        }

        fun filter(check: (FriendRequest) -> Info) {
                requests =
                        requests?.filter { value -> !((friends?.userFriends?.contains(check(value))) ?: false) }?.toMutableList()

        }

        fun indexOf(id: String) = (friends?.userFriends?.indexOfFirst { it.pk == id } ?: -1)
}

