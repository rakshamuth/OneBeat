import json
from django.shortcuts import render
from django.shortcuts import redirect
from django.http import JsonResponse
from .models import User
from .models import Song
from .models import Room
from .models import Playlist
from .models import Member

def getRoom2(request):
	roomId = request.GET['id']
	
	if (Room.objects.filter(id = roomId).exists()):
		room = Room.objects.get(id = roomId)
		playlist = Playlist.objects.filter(room = room).values()
		members = Member.objects.filter(room = room).values('user')
		songsId = [p['song_id'] for p in playlist]
		
		return JsonResponse({
			'info' : 'room',
			'id' : room.id,
			'creator' : room.creator.userId,
			'name' : room.name,
			'password' : room.password,
			'songs' : [ { 
				'artist' : Song.objects.get(id = songId).artist,
				'title' : Song.objects.get(id = songId).title,
				'duration' : Song.objects.get(id = songId).duration,
				'spotifyRef' : Song.objects.get(id = songId).spotifyRef
			} for songId in songsId],
			'addedBy' : [User.objects.get(userId = p['addedBy_id']).name for p in playlist],
			'members' : [d['user'] for d in members]
			})
	
	else:
		return JsonResponse({
			'error':'room does not exist',
			'id' : roomId
			})
		