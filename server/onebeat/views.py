import json
from django.shortcuts import render
from django.shortcuts import redirect
from django.http import JsonResponse
from .models import User
from .models import Song
from .models import Room
from .models import Playlist
from .models import Member

def addUser(request):
	received_json_data = json.loads(request.POST['request'])
	userId = received_json_data['id']
	name = received_json_data['name']
	
	if (User.objects.filter(userId = userId).exists()):
		return JsonResponse({
			'added' : False,
			'error' : 'user already exists',
			'id' : userId
			})
	
	else:
		User.objects.create(
			userId = userId,
			name = name
			)
		
		return JsonResponse({
			'added' : True,
			'id' : userId
			})


def getUser(request):
	userId = request.GET['id']
	
	if (User.objects.filter(userId = userId).exists()):
		user = User.objects.get(userId = userId)
		rooms = Member.objects.filter(user = userId).values('room')
		roomsId = [r['room'] for r in rooms]
		
		return JsonResponse({
			'info' : 'user',
			'id' : user.userId,
			'name' : user.name,
			'rooms' : [ { 
				'id' : roomId,
				'name' : Room.objects.get(id = roomId).name
				} for roomId in roomsId]
			})
	
	else:
		return JsonResponse({
			'error' : 'user does not exist',
			'id' : userId
			})


def addSong(request):
	received_json_data = json.loads(request.POST['request'])
	
	userId = received_json_data['addedBy']
	roomId = received_json_data['room']

	if ( User.objects.filter(userId = userId).exists() ):
		addedBy = User.objects.get(userId = userId)
		
		if ( Room.objects.filter(id = roomId).exists() ):
			room = Room.objects.get(id = roomId)
			spotifyRef = received_json_data['spotifyRef']


			#add the song to the DB
			if ( not(Song.objects.filter(spotifyRef = spotifyRef).exists()) ):
				title = received_json_data['title']
				artist = received_json_data['artist']
				duration = received_json_data['duration']
				
				song = Song.objects.create(
					artist = artist,
					title = title,
					duration = duration,
					spotifyRef = spotifyRef,
				)

			song = Song.objects.get(spotifyRef = spotifyRef)
			Playlist.objects.create(
				room = room,
				song = song,
				addedBy = addedBy
				)
			
			return JsonResponse({
				'added' : True,
				'song' : song.id,
				'room' : room.id,
				'addedBy' : addedBy.userId
				})
		
		else:
			return JsonResponse({
				'added' : False,
				'error' : 'room does not exist',
				'id' : roomId
				})
	
	else:
		return JsonResponse({
			'added' : False,
			'error' : 'user does not exist',
			'id' : userId
			})


def getSong(request):
	songId = request.GET['id']
	
	if (Song.objects.filter(id = songId).exists()):
		song = Song.objects.get(id = songId)
		
		return JsonResponse({
			'info' : 'song', 
			'id' : song.songId,
			'artist' : song.artist, 
			'title' : song.title, 
			'duration' : song.duration, 
			'spotifyRef' : song.spotifyRef
			})
	
	else:
		return JsonResponse({
			'error' : 'song does not exist',
			'id' : songId
			})


def createRoom(request):
	received_json_data = json.loads(request.POST['request'])
	name = received_json_data['name']
	creatorId = received_json_data['creator']
	
	if (Room.objects.filter(name = name).exists()):
		room = Room.objects.get(name = name)
		
		return JsonResponse({
			'added' : False,
			'error' : 'room already exists',
			'id' : room.id
			})
	
	elif (User.objects.filter(userId = creatorId).exists()):
		creator = User.objects.get(userId = creatorId)
		password = received_json_data['password']
		
		newRoom = Room.objects.create(
			name = name,
			creator = creator,
			password = password
			)
		
		Member.objects.create(
			user = creator,
			room = newRoom
			)
		
		return JsonResponse({
			'added':True,
			'id' : newRoom.id
			})
	
	else:
		return JsonResponse({
			'added' : False,
			'error':'creator does not exist',
			'id' : creatorId
			})


def getRoom(request):
	roomId = request.GET['id']
	
	if (Room.objects.filter(id = roomId).exists()):
		room = Room.objects.get(id = roomId)
		playlist = Playlist.objects.filter(room = room).order_by('id')
		members = Member.objects.filter(room = room).values('user')
		
		return JsonResponse({
			'info' : 'room',
			'id' : room.id,
			'creator' : room.creator.userId,
			'name' : room.name,
			'playlist' : [p.song.id for p in playlist],
			'addedBy' : [p.addedBy.name for p in playlist],
			'members' : [m['user'] for m in members]
			})
	
	else:
		return JsonResponse({
			'error':'room does not exist',
			'id' : roomId
			})


def joinRoom(request):
	received_json_data = json.loads(request.POST['request'])
	roomName = received_json_data['name']
	userId = received_json_data['user']
	
	if (Room.objects.filter(name = roomName).exists()):
		
		if (User.objects.filter(userId = userId).exists()):
			user = User.objects.get(userId = userId)
			room = Room.objects.get(name = roomName)
			password = received_json_data['password']
			
			if (password == room.password):
				if ( not(Member.objects.filter(user = user).filter(room = room).exists())):
					Member.objects.create(
						user = user,
						room = room
						)
				
				return JsonResponse({
					'added' : True,
					'roomId' : room.id,
					'userId' : user.userId,
					})
			
			else:
				return JsonResponse({
					'added' : False,
					'error' : 'wrong password',
					'room' : room.id,
					'password' : password
					})
		
		else:
			return JsonResponse({
				'added' : False,
				'error' : 'user does not exist',
				'id' : userId
				})
	
	else:
		return JsonResponse({
			'added' : False,
			'error' : 'room does not exist',
			'room' : roomName
			})

def searchRoom(request):
	received_json_data = json.loads(request.POST['request'])
	nameSearch = received_json_data['name']

	rooms = Rooms.objects.filter(name__contains = nameSearch).values('name')

	return JsonResponse({ 'rooms' : [r['name'] for r in rooms] })