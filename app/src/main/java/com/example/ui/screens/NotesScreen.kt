package com.example.ui.screens

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import com.example.ui.components.AnimateEntrance
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Note
import com.example.ui.components.SubjectTag
import com.example.ui.components.VybeButton
import com.example.ui.components.VybeCard
import com.example.ui.components.VybeScreenBackground
import com.example.ui.components.VybeText
import com.example.ui.theme.CardStylePreset
import com.example.ui.theme.LocalVybeTheme
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    myNotes: List<Note>,
    onAddNote: (String, String, String, Boolean, String?) -> Unit,
    onDeleteNote: (Int) -> Unit,
    onExportPdf: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalVybeTheme.current
    val context = LocalContext.current
    var showCreateDialog by remember { mutableStateOf(false) }

    // Dialog form state
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }

    // Finger drawing canvas lines tracking
    val drawingLines = remember { mutableStateListOf<List<Offset>>() }
    var currentLine = remember { mutableStateOf<List<Offset>>(emptyList()) }

    VybeScreenBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Notes Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    VybeText(
                        text = "MY STUDY LAB",
                        style = TextStyle(
                            fontSize = theme.headingFontSize,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp
                        ),
                        color = theme.accentColor
                    )
                    VybeText(
                        text = "Keep track of your private or public study notes",
                        style = TextStyle(fontSize = 13.sp),
                        color = theme.onBackgroundColor.copy(alpha = 0.6f)
                    )
                }

                // Create Note floating-trigger button
                IconButton(
                    onClick = {
                        // Clear previous dialog state
                        title = ""
                        subject = ""
                        content = ""
                        isPublic = true
                        drawingLines.clear()
                        showCreateDialog = true
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(theme.cardCornerRadius))
                        .background(theme.accentColor)
                        .testTag("create_note_dialog_trigger")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Note",
                        tint = if (theme.accentColorHex == "#FFFFFF") Color.Black else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Notes grid list
            if (myNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.EditNote,
                            contentDescription = "No Notes",
                            tint = theme.accentColor.copy(alpha = 0.4f),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        VybeText(
                            text = "Your notebook is empty.",
                            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        )
                        VybeText(
                            text = "Tap the '+' button above to snap a sketch note!",
                            style = TextStyle(fontSize = 12.sp),
                            color = theme.onBackgroundColor.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    itemsIndexed(myNotes) { index, note ->
                        AnimateEntrance(index = index) {
                            VybeCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SubjectTag(subject = note.subject)

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        // One-tap PDF export
                                        Icon(
                                            imageVector = Icons.Default.PictureAsPdf,
                                            contentDescription = "Export PDF",
                                            tint = theme.accentColor,
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clickable { onExportPdf(note) }
                                        )

                                        // Delete note
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete Note",
                                            tint = theme.onBackgroundColor.copy(alpha = 0.4f),
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clickable { onDeleteNote(note.id) }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                VybeText(
                                    text = note.title,
                                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                )

                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (note.isPublic) Icons.Default.Public else Icons.Default.Lock,
                                        contentDescription = "Visibility",
                                        tint = theme.onBackgroundColor.copy(alpha = 0.4f),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    VybeText(
                                        text = if (note.isPublic) "Public Study Feed" else "Private Notebook",
                                        style = TextStyle(fontSize = 10.sp),
                                        color = theme.onBackgroundColor.copy(alpha = 0.5f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                VybeText(
                                    text = note.content,
                                    style = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                                    color = theme.onBackgroundColor.copy(alpha = 0.7f),
                                    maxLines = 6
                                )
                            }
                        }
                    }
                }
            }
        }

        // Custom full-screen-ish study sheet creator Dialog
        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(theme.cardCornerRadius),
                containerColor = theme.surfaceColor,
                title = {
                    VybeText(
                        text = "NEW STUDY SHEET",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Black),
                        color = theme.accentColor
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Title e.g. Quantum Computing Intro", fontSize = 13.sp, fontFamily = theme.fontFamily) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("note_title_input"),
                            shape = RoundedCornerShape(theme.cardCornerRadius),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = theme.onBackgroundColor,
                                unfocusedTextColor = theme.onBackgroundColor,
                                focusedBorderColor = theme.accentColor,
                                unfocusedBorderColor = theme.onBackgroundColor.copy(alpha = 0.2f)
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            placeholder = { Text("Subject e.g. Physics", fontSize = 13.sp, fontFamily = theme.fontFamily) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("note_subject_input"),
                            shape = RoundedCornerShape(theme.cardCornerRadius),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = theme.onBackgroundColor,
                                unfocusedTextColor = theme.onBackgroundColor,
                                focusedBorderColor = theme.accentColor,
                                unfocusedBorderColor = theme.onBackgroundColor.copy(alpha = 0.2f)
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            placeholder = { Text("Type summary notes, cheatsheets, or formulas...", fontSize = 13.sp, fontFamily = theme.fontFamily) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .testTag("note_content_input"),
                            shape = RoundedCornerShape(theme.cardCornerRadius),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = theme.onBackgroundColor,
                                unfocusedTextColor = theme.onBackgroundColor,
                                focusedBorderColor = theme.accentColor,
                                unfocusedBorderColor = theme.onBackgroundColor.copy(alpha = 0.2f)
                            )
                        )

                        // INTERACTIVE SKETCHPAD / NOTE CANVAS DRAWING
                        VybeText(
                            text = "DRAW / SKETCH DIGITALLY (TOUCH)",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            color = theme.accentColor
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(theme.cardCornerRadius))
                                .background(theme.onBackgroundColor.copy(alpha = 0.05f))
                                .border(1.dp, theme.onBackgroundColor.copy(alpha = 0.15f), RoundedCornerShape(theme.cardCornerRadius))
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            currentLine.value = listOf(offset)
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            val next = currentLine.value + change.position
                                            currentLine.value = next
                                        },
                                        onDragEnd = {
                                            drawingLines.add(currentLine.value)
                                            currentLine.value = emptyList()
                                        }
                                    )
                                }
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Draw completed lines
                                drawingLines.forEach { line ->
                                    if (line.size > 1) {
                                        val path = Path().apply {
                                            moveTo(line[0].x, line[0].y)
                                            for (i in 1 until line.size) {
                                                lineTo(line[i].x, line[i].y)
                                            }
                                        }
                                        drawPath(
                                            path = path,
                                            color = theme.accentColor,
                                            style = Stroke(width = 4f, cap = StrokeCap.Round)
                                        )
                                    }
                                }
                                // Draw current active line
                                val current = currentLine.value
                                if (current.size > 1) {
                                    val path = Path().apply {
                                        moveTo(current[0].x, current[0].y)
                                        for (i in 1 until current.size) {
                                            lineTo(current[i].x, current[i].y)
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        color = theme.accentColor,
                                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                                    )
                                }
                            }

                            // Clean Canvas Button
                            IconButton(
                                onClick = { drawingLines.clear() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(28.dp)
                                    .background(theme.backgroundColor.copy(alpha = 0.8f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Clear Canvas",
                                    tint = theme.accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Share public/private toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            VybeText(
                                text = "Share publicly on community feed",
                                style = TextStyle(fontSize = 12.sp)
                            )
                            Switch(
                                checked = isPublic,
                                onCheckedChange = { isPublic = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = theme.accentColor,
                                    checkedTrackColor = theme.accentColor.copy(alpha = 0.4f)
                                )
                            )
                        }
                    }
                },
                confirmButton = {
                    VybeButton(
                        onClick = {
                            if (title.trim().isNotEmpty() && content.trim().isNotEmpty()) {
                                // Draw state representation or path
                                val imageTag = if (drawingLines.isNotEmpty()) "has_drawing" else null
                                onAddNote(title, subject, content, isPublic, imageTag)
                                showCreateDialog = false
                            }
                        },
                        enabled = title.trim().isNotEmpty() && content.trim().isNotEmpty(),
                        testTag = "save_note_button"
                    ) {
                        VybeText(
                            text = "COMPILE NOTES",
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                            color = if (theme.accentColorHex == "#FFFFFF" || theme.preset == LocalVybeTheme.current.preset) Color.Black else Color.White
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        VybeText(text = "CANCEL", style = TextStyle(fontSize = 12.sp))
                    }
                }
            )
        }
    }
}

// 1-Tap PDF Generator Utility
fun exportNoteToPdf(context: Context, note: Note) {
    try {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 dimensions
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paintTitle = Paint().apply {
            textSize = 28f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }

        val paintSubject = Paint().apply {
            textSize = 14f
            textSkewX = -0.25f
            color = android.graphics.Color.GRAY
        }

        val paintBody = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.DKGRAY
        }

        // Draw note contents
        canvas.drawText("VYBE DIGITAL STUDY LAB", 50f, 60f, paintSubject)
        canvas.drawText(note.title, 50f, 100f, paintTitle)
        canvas.drawText("Subject: ${note.subject.uppercase()}  |  Creator: @${note.authorHandle}", 50f, 130f, paintSubject)

        canvas.drawLine(50f, 150f, 545f, 150f, paintBody)

        var yOffset = 180f
        val lines = note.content.split("\n")
        lines.forEach { line ->
            // Simple auto text wrap lines
            if (line.length > 80) {
                val chunks = line.chunked(80)
                chunks.forEach { chunk ->
                    canvas.drawText(chunk, 50f, yOffset, paintBody)
                    yOffset += 18f
                }
            } else {
                canvas.drawText(line, 50f, yOffset, paintBody)
                yOffset += 18f
            }
        }

        // Draw visual note sketch indicator
        if (note.imagePath != null) {
            yOffset += 30f
            canvas.drawRect(50f, yOffset, 545f, yOffset + 120f, Paint().apply {
                color = android.graphics.Color.LTGRAY
                style = Paint.Style.STROKE
                strokeWidth = 2f
            })
            canvas.drawText("[ DIGITAL TOUCH SKETCH ATTACHED ]", 180f, yOffset + 65f, paintSubject)
        }

        pdfDocument.finishPage(page)

        // Write to external documents or downloads
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${note.title.replace(" ", "_")}_Cheatsheet.pdf")
        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()

        Toast.makeText(context, "Exported successfully: ${file.name}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "PDF compilation failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
