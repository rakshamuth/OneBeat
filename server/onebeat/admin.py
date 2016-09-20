from django.contrib import admin
from .models import User
from .models import Room
from .models import Song
from .models import Playlist
from .models import Member

admin.site.register(User)
admin.site.register(Room)
admin.site.register(Song)
admin.site.register(Playlist)
admin.site.register(Member)