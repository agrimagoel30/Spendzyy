package com.agrima.spendzyy.screens

import android.R.attr.description
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agrima.spendzyy.ui.theme.CardBgColor
import com.agrima.spendzyy.ui.theme.PrimaryTextColor
import com.agrima.spendzyy.ui.theme.ScreenBgColor
import com.agrima.spendzyy.ui.theme.SubTextColor
import com.agrima.spendzyy.viewmodel.NotesViewModel
import com.google.ai.client.generativeai.type.content
import com.agrima.spendzyy.data.local.entity.NoteEntity


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NotesScreen(
//    viewModel: NotesViewModel
//
//) {
//
//    val notes by viewModel.notes.collectAsState()
//
//    val noteColors = listOf(
//        Color(0xFFFFF9C4), // Light Yellow
//        Color(0xFFC8E6C9), // Light Green
//        Color(0xFFBBDEFB), // Light Blue
//        Color(0xFFFFCDD2), // Light Red
//        Color(0xFFD1C4E9), // Light Purple
//        Color(0xFFFFE0B2)  // Light Orange
//    )
//
//    var showAddDialog by remember { mutableStateOf(false) }
//    var editNoteIndex by remember { mutableStateOf<Int?>(null) }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = { showAddDialog = true }) {
//                Icon(Icons.Default.Add, contentDescription = "Add Note")
//            }
//        }
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(MaterialTheme.colorScheme.background)
//                .padding(16.dp)
//        ) {
//
//            Text(
//                text = "Notes",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (notes.isEmpty()) {
//                Text("No notes added yet")
//            } else {
//                LazyColumn {
//                    itemsIndexed(notes) { index, note ->
//                        val bgColor = noteColors[index % noteColors.size]
//
//                        NoteCard(
//                            note = note,
//                            backgroundColor = bgColor,
//                            onEdit = { editNoteIndex = index },
//                            onDelete = { notes.removeAt(index) }
//                        )
//                        Spacer(modifier = Modifier.height(12.dp))
//                    }
//                }
//            }
//        }
//    }
//
//    /* ---------- ADD NOTE ---------- */
//    if (showAddDialog) {
//        AddOrEditNoteDialog(
//            titleText = "",
//            descriptionText = "",
//            onDismiss = { showAddDialog = false },
//            onSave = { title, desc ->
//                viewModel.addNote(
//                    title = title,
//                    content = description
//                )
//
//                showAddDialog = false
//            }
//        )
//    }
//
//    /* ---------- EDIT NOTE ---------- */
//    editNoteIndex?.let { index ->
//        val note = notes[index]
//
//        AddOrEditNoteDialog(
//            titleText = note.title,
//            descriptionText = note.description,
//            onDismiss = { editNoteIndex = null },
//            onSave = { title, desc ->
//                notes[index] = Note(title, desc)
//                editNoteIndex = null
//            }
//        )
//    }
//}
//
///* ---------------- NOTE CARD ---------------- */
//
//@Composable
//fun NoteCard(
//    note: Note,
//    backgroundColor: Color,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit
//) {
//
//    var expanded by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { expanded = !expanded },
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Text(note.title, fontWeight = FontWeight.SemiBold,color = MaterialTheme.colorScheme.onSurface)
//
//                Row {
//                    IconButton(onClick = onEdit) {
//                        Icon(Icons.Default.Edit, contentDescription = "Edit")
//                    }
//                    IconButton(onClick = onDelete) {
//                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
//                    }
//                    Icon(
//                        imageVector = if (expanded)
//                            Icons.Default.ExpandLess
//                        else
//                            Icons.Default.ExpandMore,
//                        contentDescription = null
//                    )
//                }
//            }
//
//            AnimatedVisibility(visible = expanded) {
//                Column {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Divider()
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(note.description, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
//                }
//            }
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel
) {

    val notes by viewModel.notes.collectAsState()

    val noteColors = listOf(
        Color(0xFFFFF9C4),
        Color(0xFFC8E6C9),
        Color(0xFFBBDEFB),
        Color(0xFFFFCDD2),
        Color(0xFFD1C4E9),
        Color(0xFFFFE0B2)
    )

    var showAddDialog by remember { mutableStateOf(false) }
    var editNote by remember { mutableStateOf<NoteEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Notes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (notes.isEmpty()) {
                Text("No notes added yet")
            } else {
                LazyColumn {
                    itemsIndexed(notes) { index, note ->
                        val bgColor = noteColors[index % noteColors.size]

                        NoteCard(
                            note = note,
                            backgroundColor = bgColor,
                            onEdit = { editNote = note },
                            onDelete = {
                                viewModel.deleteNote(note)   // ✅ ROOM DELETE
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    /* ---------- ADD NOTE ---------- */
    if (showAddDialog) {
        AddOrEditNoteDialog(
            titleText = "",
            descriptionText = "",
            onDismiss = { showAddDialog = false },
            onSave = { title, desc ->
                viewModel.addNote(title, desc)   // ✅ ROOM INSERT
                showAddDialog = false
            }
        )
    }

    /* ---------- EDIT NOTE ---------- */
    editNote?.let { note ->
        AddOrEditNoteDialog(
            titleText = note.title,
            descriptionText = note.content,
            onDismiss = { editNote = null },
            onSave = { title, desc ->
                viewModel.updateNote(
                    note.copy(
                        title = title,
                        content = desc
                    )
                )
                editNote = null
            }
        )
    }
}


@Composable
fun NoteCard(
    note: NoteEntity,
    backgroundColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(note.title, fontWeight = FontWeight.SemiBold)

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(note.content)
                }
            }
        }
    }
}

/* ---------------- ADD / EDIT DIALOG ---------------- */

@Composable
fun AddOrEditNoteDialog(
    titleText: String,
    descriptionText: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var title by remember { mutableStateOf(titleText) }
    var description by remember { mutableStateOf(descriptionText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (titleText.isEmpty()) "Add Note" else "Edit Note",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, description)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
