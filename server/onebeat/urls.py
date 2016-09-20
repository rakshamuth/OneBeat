from django.conf.urls import url
from . import views
from. import views2
from django.contrib import admin

urlpatterns = [
    #url(r'^admin/', include(admin.site.urls)),
    #url(r'^onebeat/', include('onebeat.urls')),
    url(r'^getUser/', views.getUser, name='getUser'),
    url(r'^addUser/', views.addUser, name='addUser'),
    url(r'^getSong/', views.getSong, name='getSong'),
    url(r'^addSong/', views.addSong, name='addSong'),
    url(r'^getRoom/', views.getRoom, name='getRoom'),
    url(r'^createRoom/', views.createRoom, name='createRoom'),
    url(r'^joinRoom/', views.joinRoom, name='joinRoom'),
    url(r'^searchRoom/', views.searchRoom, name='searchRoom'),
	url(r'^getRoom2/', views2.getRoom2, name='getRoom2'),
]